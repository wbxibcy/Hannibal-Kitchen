from fastapi import FastAPI, HTTPException, Depends, Query
from sqlalchemy.orm import Session
from database import SessionLocal, engine
from models import User, Dish, Log, Ingredient
from schemas import LoginRequest, DishRequest, LogResponse, SearchResponse, MeResponse
from models import Base
from typing import List
# from sqlalchemy.orm import aliased
from Levenshtein import distance as levenshtein_distance


# 创建数据库表
Base.metadata.create_all(bind=engine)

app = FastAPI()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@app.post("/login")
def login(request: LoginRequest, db: Session = Depends(get_db)):
    print(request.account)
    print(request.password)
    user = db.query(User).filter(User.account == request.account, User.password == request.password).first()
    if user:
        return {"user_id": user.id}
    else:
        raise HTTPException(status_code=401, detail="Invalid credentials")


@app.post("/dish", response_model=dict)
def create_dish(data: dict, db: Session = Depends(get_db)):
    print(data)
    # 创建一个字典来存储每个食材的累计数量和单位
    ingredient_details = {}

    # 遍历输入 JSON 中的每道菜
    for dish_data in data.get("dishes", []):
        dish_name = dish_data.get("dish_name")
        ingredients = dish_data.get("ingredients", {})

        print(dish_name)
        print(ingredients)

        # 检查菜是否已存在
        existing_dish = db.query(Dish).filter(Dish.name == dish_name).first()

        if not existing_dish:
            # 如果不存在，创建新的 Dish
            new_dish = Dish(name=dish_name)
            db.add(new_dish)
            db.commit()
            db.refresh(new_dish)
        else:
            # 如果存在，使用 merge 将对象附加到会话中
            new_dish = db.merge(existing_dish)

        # 遍历每道菜中的每个食材
        for ingredient_name, data in ingredients.items():
            # 获取食材的数量和单位
            quantity = data.get("quantity", 0)
            unit = data.get("unit", "")

            # 检查食材是否已存在
            existing_ingredient = db.query(Ingredient).filter(Ingredient.name == ingredient_name).first()

            if not existing_ingredient:
                # 如果不存在，创建新的 Ingredient
                new_ingredient = Ingredient(name=ingredient_name)
                db.add(new_ingredient)
                db.commit()
                db.refresh(new_ingredient)
            else:
                # 如果存在，使用 merge 将对象附加到会话中
                new_ingredient = db.merge(existing_ingredient)

            # 更新食材的累计数量和单位
            if ingredient_name in ingredient_details:
                ingredient_details[ingredient_name]["quantity"] += quantity
            else:
                ingredient_details[ingredient_name] = {"quantity": quantity, "unit": unit}

            # 为 Dish 和 Ingredient 的组合创建日志条目
            log_entry = Log(
                user_id=data.get("user_id", None), 
                dish_id=new_dish.id,
                ingredient_id=new_ingredient.id,
                quantity=quantity,
                unit=unit,
            )
            db.add(log_entry)
            db.commit()
            db.refresh(log_entry)

    # 返回一个 JSON 响应，其中包含每个食材的累计数量和单位
    return {
        "ingredient_details": ingredient_details
    }


@app.get("/log", response_model=List[LogResponse])
async def get_log(user_id: int = Query(..., description="User ID"),
                   db: Session = Depends(get_db)):
    # 查询用户的日志
    logs = db.query(Log).join(Dish).filter(Log.user_id == user_id).all()
    if logs:
        # 使用集合来去除重复的 dish_name
        unique_dish_names = set(log.dish.name for log in logs)
        
        # 使用列表推导式构建 LogResponse 列表
        log_responses = [LogResponse(dish_name=dish_name) for dish_name in unique_dish_names]
        return log_responses
    else:
        raise HTTPException(status_code=404, detail="Log not found")


@app.get("/search", response_model=List[SearchResponse])
def search_dish(content: str = Query(..., description="Content"), user_id: int = Query(..., description="User ID"),
                db: Session = Depends(get_db)):
    # 从数据库查询所有日志
    logs = db.query(Log).join(Dish).filter(Log.user_id == user_id).all()

    # 使用Levenshtein距离找到相似的菜名
    相似度阈值 = 3  # 您可以根据需要调整此阈值
    similar_logs = [
        log for log in logs if levenshtein_distance(content, log.dish.name) <= 相似度阈值
    ]

    if similar_logs:
        # 根据菜名去除重复项
        unique_dish_names = {log.dish.name for log in similar_logs}

        # 构建SearchResponse列表
        search_responses = [SearchResponse(dish_name=dish_name) for dish_name in unique_dish_names]
        return search_responses
    else:
        raise HTTPException(status_code=404, detail="未找到菜品")


@app.get("/me", response_model=MeResponse)
def get_me(user_id: int = Query(..., description="User ID"), db: Session = Depends(get_db)):
    # 查询用户的个人信息
    user = db.query(User).filter(User.id == user_id).first()
    if user:
        return MeResponse(
            user_id=user.id,
            name=user.name,
            nickname=user.nickname,
            sex=user.sex,
            account=user.account,
            phone=user.phone,
        )
    else:
        raise HTTPException(status_code=404, detail="User not found")


from pydantic import BaseModel

class LoginRequest(BaseModel):
    account: str
    password: str

class DishRequest(BaseModel):
    name: str

class LogResponse(BaseModel):
    dish_name: str

class SearchResponse(BaseModel):
    dish_name: str

class MeResponse(BaseModel):
    user_id: int
    name: str
    nickname: str
    sex: str
    account: str
    phone: str

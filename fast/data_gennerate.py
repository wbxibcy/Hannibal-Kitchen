from sqlalchemy import create_engine, text

# 连接数据库
database_url = "mysql+mysqlconnector://root:powerful@47.100.125.229/HH"
engine = create_engine(database_url)

# 定义 SQL 语句列表
sql_statements = [
    """
    INSERT INTO users (name, nickname, sex, account, phone, password)
    VALUES 
      ('张三', '三三', '男', 'zhangsan@123.com', '1234567890', 'password123'),
      ('王五', '呜呜呜', '男', 'wangwu@789.com', '5555555555', 'password789');
    """,
    """
    INSERT INTO ingredients (name)
    VALUES 
      ('番茄'), ('鸡蛋'), ('油'),
      ('鸡肉'), ('花生'), ('辣椒');
    """,
    """
    INSERT INTO dishs (name)
    VALUES 
      ('番茄炒蛋'), ('宫保鸡丁'),
      ('麻辣鸡块'), ('花生鸡丁');
    """
]

with engine.connect() as connection:
    for statement in sql_statements:
        print(statement)
        result = connection.execute(text(statement))

    # 提交事务
    connection.commit()

# 查询所有表的数据
tables = ['users', 'dishs', 'ingredients', 'logs']

for table in tables:
    with engine.connect() as connection:
        result = connection.execute(text(f"SELECT * FROM {table};")).fetchall()
        print(f"查询表 {table} 的结果：", result)

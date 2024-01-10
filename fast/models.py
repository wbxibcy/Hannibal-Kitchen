from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float
from sqlalchemy.orm import relationship

from database import Base

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    name = Column(String(255))
    nickname = Column(String(255))
    sex = Column(String(255))
    account = Column(String(255))
    phone = Column(String(255))
    password = Column(String(255))

    # Establishing a one-to-many relationship with logs
    logs = relationship('Log', back_populates='user')

class Dish(Base):
    __tablename__ = 'dishs'

    id = Column(Integer, primary_key=True)
    name = Column(String(255))

    # Establishing a one-to-many relationship with logs
    logs = relationship('Log', back_populates='dish')

class Ingredient(Base):
    __tablename__ = 'ingredients'

    id = Column(Integer, primary_key=True)
    name = Column(String(255))

    # Establishing a one-to-many relationship with logs
    logs = relationship('Log', back_populates='ingredient')

class Log(Base):
    __tablename__ = 'logs'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    dish_id = Column(Integer, ForeignKey('dishs.id'))
    ingredient_id = Column(Integer, ForeignKey('ingredients.id'))
    quantity = Column(Float)
    unit = Column(String(255))

    user = relationship('User', back_populates='logs')
    dish = relationship('Dish', back_populates='logs')
    ingredient = relationship('Ingredient', back_populates='logs')

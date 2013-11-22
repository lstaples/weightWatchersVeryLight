USE master
go

if exists (SELECT name from sysdatabases where name = 'weightWatchersVeryLight')
BEGIN
	--kill any connections to the current db so we can drop it 
	ALTER DATABASE weightWatchersVeryLight SET SINGLE_USER WITH ROLLBACK IMMEDIATE
	DROP DATABASE weightWatchersVeryLight
END
GO
 
CREATE DATABASE weightWatchersVeryLight 
GO
ALTER DATABASE weightWatchersVeryLight MODIFY FILE 
( NAME = N'weightWatchersVeryLight' , SIZE = 3048KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
GO
ALTER DATABASE weightWatchersVeryLight MODIFY FILE 
( NAME = N'weightWatchersVeryLight_log' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO

USE weightWatchersVeryLight
GO

                     
CREATE TABLE users(id int primary key identity
				  ,version int 
                  ,username varchar(50)
                  ,password varchar(1000)
                  ,enable bit
                  ,accountExpired bit
                  ,accountLocked bit
                  ,passwordExpired bit)

CREATE TABLE roles(id int primary key identity
				  ,version int
				  ,authority varchar(100))
				  
CREATE TABLE userRole(id int primary key identity
				      ,version int
				      ,userID int references users(ID)
				      ,roleID int references roles(ID))

CREATE TABLE food (id int primary key identity
                   ,version int
                   ,userID int references users(id)
                   ,name varchar(255) not null
                   )
                   
CREATE TABLE portion(id int primary key identity
                     ,version int
                     ,foodID int references food(id) not null
                     ,description varchar(255) not null
                     ,calories int not null CHECK (calories >0))
                     
CREATE TABLE recipe(id int primary key identity
                    ,version int
                    ,userID int references users(id)
                    ,name varchar(255) not null
                    ,calories int not null CHECK (calories >0)
                    ,servings int not null CHECK (servings >0))

CREATE TABLE ingredient(id int primary key identity
                       ,version int
                       ,recipeID int references recipe(id) not null 
                       ,portionID int references portion(id) not null 
                       ,calories int not null CHECK (calories >0)
                       ,quantity int not null CHECK (quantity >0)
                       ) 

CREATE TABLE calorieIntakeLogEntry(id int primary key identity
                     ,version int
                     ,userID int references users(id)
                     ,dateEaten datetime
                     ,meal varchar(10) not null
                     ,quantity int not null
                     ,calories int not null CHECK (calories >0)
                     ,entryType varchar(20))
                     
CREATE TABLE foodLogEntry(id int primary key
                          ,version int
                          ,portionID int references portion(id) not null
                          ,foreign key (id) references calorieIntakeLogEntry(id))
                          
CREATE TABLE recipeLogEntry(id int primary key
                          ,version int
                          ,recipeID int references recipe(id) not null
                          ,foreign key (id) references calorieIntakeLogEntry(id))

CREATE TABLE weightLogEntry(id int primary key identity
					 ,userID int references users(id)
                     ,version int 
                     ,dateWeighed datetime
                     ,weight int CHECK (weight >0))

                       
GO

CREATE INDEX ix_portion_foodID ON portion (foodID)

CREATE INDEX ix_ingredient_composite ON ingredient (recipeID,portionID)

CREATE INDEX ix_calorieIntakeLogEntry_dateEaten ON calorieIntakeLogEntry (dateEaten)

CREATE INDEX ix_weightLog_dateWeighed ON weightLogEntry (dateWeighed)


                    
                     



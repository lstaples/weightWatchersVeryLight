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

CREATE TABLE food (id int primary key identity
                   ,version int
                   ,name varchar(255) not null
                   )
                   
CREATE TABLE portion(id int primary key identity
                     ,version int
                     ,foodID int references food(id) not null
                     ,description varchar(255) not null
                     ,calories int not null CHECK (calories >0))
                     
CREATE TABLE recipe(id int primary key identity
                    ,version int
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

CREATE TABLE foodLog(id int primary key identity
                     ,version int
                     ,dateEaten datetime
                     ,recipeID int references recipe(id)
                     ,foodID int references food(id) 
                     ,meal varchar(10) not null
                     ,quantity int not null
                     ,calories int not null CHECK (calories >0))

CREATE TABLE weightLog(id int primary key identity
                     ,version int 
                     ,dateWeighed datetime
                     ,weight int CHECK (weight >0))
                       
GO

CREATE INDEX ix_portion_foodID ON portion (foodID)
CREATE INDEX ix_ingredient_composite ON ingredient (recipeID,portionID)
CREATE INDEX ix_foodLog_dateEaten ON foodLog (dateEaten)
CREATE INDEX ix_weightLog_dateWeighed ON weightLog (dateWeighed)


                    
                     



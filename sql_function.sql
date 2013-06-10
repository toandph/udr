CREATE FUNCTION strSplit(x varchar(255), delim varchar(12), pos int) returns varchar(255)
    return replace(substring(substring_index(x, delim, pos), length(substring_index(x, delim, pos - 1)) + 1), delim, '');
    
DELIMITER //
DROP FUNCTION `project_125`.`GET_STATE` //

DELIMITER //
CREATE FUNCTION `project_125`.`GET_STATE`(trend VARCHAR(1024), bOrder INT)
  RETURNS VARCHAR(100)
  BEGIN
    IF strSplit(trend, '#', 1) <> '' THEN    
        IF strSplit(trend, '#', 2) = '' THEN
            IF bOrder = 1 AND strSplit(trend, '#', 1) = '1' THEN
                RETURN 'NEW';
            ELSEIF bOrder = 1 THEN
                RETURN 'UNKNOWN';
            END IF;
        ELSE
            IF bOrder = 1 AND strSplit(trend, '#', 1) = '1' THEN
                RETURN 'NEW';
            ELSEIF bOrder = 1 THEN
                RETURN 'UNKNOWN';
            END IF;
            
            IF strSplit(trend, '#', bOrder) = '1' THEN
                IF strSplit(trend, '#', bOrder-1) = '1' THEN
                    RETURN 'EXISTING';
                END IF;   
                
                SET @i = bOrder-1;
                find: LOOP
                    IF strSplit(trend, '#', @i) = '1' THEN
                        RETURN 'REOCCURED';
                    END IF;
                    SET @i = @i - 1;
                    IF (@i = 0) THEN
                        LEAVE find;
                    END IF;
                END LOOP find;                     
                
                RETURN 'NEW';
            ELSE 
                SET @i = bOrder-1;
                find: LOOP
                    IF strSplit(trend, '#', @i) = '1' THEN
                        RETURN 'FIXED';
                    END IF;
                    SET @i = @i - 1;
                    IF (@i = 0) THEN
                        LEAVE find;
                    END IF;
                END LOOP find;                                  
            END IF;
        END IF;
    END IF;
    
    RETURN 'UNKNOWN';
  END
//    

DELIMITER //
CREATE FUNCTION `project_125`.`IS_ZERO`(trend VARCHAR(1024)) RETURNS BOOLEAN
  BEGIN
    IF strSplit(trend, '#', 1) = '' THEN
        RETURN TRUE;
    ELSEIF strSplit(trend, '#', 2) = '' THEN
        IF strSplit(trend, '#', 1) = '0' THEN
            RETURN TRUE;
        ELSE 
            RETURN FALSE;
        END IF;        
    ELSE
        SET @i = 1;
        SET @s = strSplit(trend, '#', @i);
        find: LOOP
            IF @s = '1' THEN
                RETURN FALSE;
            END IF;
            SET @i = @i + 1;
            SET @s = strSplit(trend, '#', @i);
            IF @s = '' THEN
                LEAVE find;
            END IF;
        END LOOP find;           
    END IF;
    RETURN TRUE;
  END
//
SELECT IS_ZERO('')
//

DELIMITER //
DROP FUNCTION `project_125`.`REMOVE_BUILD` //


DELIMITER //
CREATE FUNCTION `project_125`.`REMOVE_BUILD`(trend VARCHAR(1024), bList VARCHAR(1024)) RETURNS VARCHAR(1024)
  BEGIN
    SET @i = 1;
    SET @s = strSplit(trend, '#', @i);
    SET @result = '';
    find: LOOP
        IF @s = '' THEN
            LEAVE find;
        END IF;
        
        SET @i2 = 1;
        SET @s2 = strSplit(bList, '#', @i2);
        SET @flag = true;
        find2: LOOP
            IF @s2 = '' THEN LEAVE find2; END IF;
            SET @delId = CONVERT(@s2, UNSIGNED INTEGER);
            IF @i = @delId THEN
                SET @flag = false;
                LEAVE find2;
            END IF;
            
            SET @i2 = @i2 + 1;
            SET @s2 = strSplit(bList, '#', @i2); 
        END LOOP find2;
        
        IF @flag = true THEN
            IF @result = '' THEN
                SET @result = CONCAT(@result, @s);
            ELSE
                SET @result = CONCAT(@result, '#', @s);
            END IF;                
        END IF;
        
        SET @i = @i + 1;
        SET @s = strSplit(trend, '#', @i);
    END LOOP find;
    
    RETURN @result;
  END
//  
SELECT REMOVE_BUILD('0#0#1#0#1#1','1#2#4') AS p

//
CREATE TRIGGER updateTrendTrigger AFTER UPDATE ON `project_125`.`issue_signature`
  FOR EACH ROW BEGIN
    IF IS_ZERO(NEW.trend) THEN
        DELETE FROM `project_125`.`issue_signature` WHERE id = NEW.id;                    
    END IF;    
  END;
//  

CREATE PROCEDURE RemoveTrend (IN bList VARCHAR(1024))
BEGIN         
    UPDATE `project_125`.`issue_signature`
    SET trend = REMOVE_BUILD(trend, bList);
END
//




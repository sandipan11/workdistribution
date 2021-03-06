# workdistribution

Please follow the below instruction before running the project.

MySQl Database Set up
-------------------------

1. Install MAMP and MySQL database version
2. run MAMP and in pop up window, click on Open WebStart Page
3. It will open a page(http://localhost:80/MAMP/)
4. Go to tools and click to phpMyAdmin
5. Click on New and create data base name work_distribution
6. Then go to SQL tab and run the following command to create tables. (Alt+ Enter to execute query)

CREATE TABLE `work_distribution`.`agent` ( `agent_name` VARCHAR(100) NOT NULL , `agent_id` VARCHAR(10) NOT NULL , `status` TINYINT(1) NOT NULL , `last_modified_date` DATETIME(6) NOT NULL , PRIMARY KEY (`agent_id`(10))) ENGINE = InnoDB;

CREATE TABLE `work_distribution`.`skill` ( id INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(20) NOT NULL , `description` TEXT NULL , `last_modified_date` DATETIME NOT NULL, PRIMARY KEY (id)) ENGINE = InnoDB;

CREATE TABLE `work_distribution`.`task` ( id INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(20) NOT NULL ,`description` TEXT NULL , `status` VARCHAR(20) NOT NULL , `last_modified_date` DATETIME(6) NOT NULL , `start_date` DATETIME(6) NOT NULL , `priority` TINYINT(2) NOT NULL , PRIMARY KEY (id)) ENGINE = InnoDB;

CREATE TABLE `work_distribution`.`agent_skill_rel` ( `skill_id` VARCHAR(6) NOT NULL , `agent_id` VARCHAR(10) NOT NULL , `last_modified_date` DATETIME(6) NOT NULL , PRIMARY KEY (`skill_id`(6), `agent_id`(10))) ENGINE = InnoDB;

CREATE TABLE `work_distribution`.`task_skill_rel` ( `task_id` VARCHAR(10) NOT NULL , `skill_id` VARCHAR(6) NOT NULL , `last_modified_date` DATETIME(6) NOT NULL , PRIMARY KEY (`task_id`(10), `skill_id`(6))) ENGINE = InnoDB;

CREATE TABLE `work_distribution`.`task_assignment` ( `task_id` VARCHAR(10) NOT NULL , `agent_id` VARCHAR(6) NOT NULL , `status` TINYINT(1) NOT NULL , `comments` TEXT NOT NULL , `start_date` DATETIME(6) NOT NULL , `end_date` DATETIME(6) NULL , `last_modified_date` DATETIME(6) NOT NULL , PRIMARY KEY (`task_id`(10), `agent_id`(6))) ENGINE = InnoDB;

ALTER TABLE `work_distribution`.`task` AUTO_INCREMENT=1001;

INSERT INTO `skill` (`id`, `name`, `description`, `last_modified_date`) VALUES ('1', 'skill1', 'This is skill 1', NOW());
INSERT INTO `skill` (`name`, `description`, `last_modified_date`) VALUES ('skill2', 'This is skill 2', NOW());
INSERT INTO `skill` (`name`, `description`, `last_modified_date`) VALUES ('skill3', 'This is skill 3', NOW());

INSERT INTO `agent` (`agent_name`, `agent_id`, `status`, `last_modified_date`) VALUES ('Agent1', '100', '1', NOW());
INSERT INTO `agent` (`agent_name`, `agent_id`, `status`, `last_modified_date`) VALUES ('Agent2', '101', '1', NOW());
INSERT INTO `agent` (`agent_name`, `agent_id`, `status`, `last_modified_date`) VALUES ('Agent3', '102', '1', NOW());
INSERT INTO `agent` (`agent_name`, `agent_id`, `status`, `last_modified_date`) VALUES ('Agent4', '103', '0', NOW());
INSERT INTO `agent` (`agent_name`, `agent_id`, `status`, `last_modified_date`) VALUES ('Agent4', '104', '1', NOW());
INSERT INTO `agent` (`agent_name`, `agent_id`, `status`, `last_modified_date`) VALUES ('Agent4', '105', '1', NOW());

INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('1', '100', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('2', '100', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('3', '100', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('1', '101', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('2', '101', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('2', '102', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('3', '102', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('1', '104', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('3', '104', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('1', '105', NOW());
INSERT INTO `agent_skill_rel` (`skill_id`, `agent_id`, `last_modified_date`) VALUES ('3', '105', NOW());


CREATE USER 'testUser'@'%' IDENTIFIED WITH mysql_native_password AS '***';GRANT SELECT, INSERT, UPDATE, DELETE, FILE ON *.* TO 'testUser'@'%' REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;GRANT ALL PRIVILEGES ON `work_distribution`.* TO 'testUser'@'%';


Enable Lombok:
--------------------------------------------------------------
1. Download lombok jar and add to eclips project build path
2. Go to command Prompt and run it (Example: java -jar lombok-1.16.10.jar). A window should appear, browse to your eclipse.exe location.
3. Click on install.
4. Launch Eclipse, update project configuration on all projects

Please import WorkDistribution.postman_collection.json in postman to test the API

To build This project:
gradlew build

Query used to select available agent:

SELECT
    HH.agent_id,
    HH.task_id,
    HH.priority,
    HH.start_date,
    agent.agent_name
FROM
    (
    SELECT
        GG.agent_id,
        GG.task_id,
        GG.priority,
        GG.start_date
    FROM
        (
        SELECT
            FF.agent_id,
            FF.task_id,
            IFNULL(FF.priority, -1) AS priority,
            FF.start_date
        FROM
            (
            SELECT
                AA.agent_id,
                AA.task_id,
                AA.priority,
                AA.start_date
            FROM
                (
                SELECT
                    D.agent_id,
                    D.task_id,
                    task.priority,
                    IFNULL(D.start_date, NOW()) AS start_date
                FROM
                    (
                    SELECT
                        C.agent_id,
                        task_assignment.task_id,
                        task_assignment.start_date,
                        task_assignment.end_date
                    FROM
                        (
                        SELECT
                            agent_id
                        FROM
                            (
                            SELECT
                                agent_id,
                                COUNT(*)
                            FROM
                                (
                                SELECT
                                    agent_id,
                                    skill_id
                                FROM
                                    agent_skill_rel
                                WHERE
                                    skill_id IN(1, 2, 3)
                            ) A
                        GROUP BY
                            agent_id
                        HAVING
                            COUNT(*) > 2
                        ) B
                    ) C
                LEFT JOIN task_assignment ON C.agent_id = task_assignment.agent_id AND task_assignment.end_date IS NULL
                ) D
            LEFT JOIN task ON D.task_id = task.id
            ORDER BY
                D.start_date) AA
                INNER JOIN(
                    SELECT BB.agent_id,
                        BB.priority
                    FROM
                        (
                        SELECT
                            E.agent_id,
                            MAX(E.priority) priority
                        FROM
                            (
                            SELECT
                                D.agent_id,
                                D.task_id,
                                task.priority,
                                task.id,
                                IFNULL(D.start_date, NOW()) AS START_DATE
                            FROM
                                (
                                SELECT
                                    C.agent_id,
                                    task_assignment.task_id,
                                    task_assignment.start_date,
                                    task_assignment.end_date
                                FROM
                                    (
                                    SELECT
                                        agent_id
                                    FROM
                                        (
                                        SELECT
                                            agent_id,
                                            COUNT(*)
                                        FROM
                                            (
                                            SELECT
                                                agent_id,
                                                skill_id
                                            FROM
                                                agent_skill_rel
                                            WHERE
                                                skill_id IN(1, 2, 3)
                                        ) A
                                    GROUP BY
                                        agent_id
                                    HAVING
                                        COUNT(*) > 2
                                    ) B
                                ) C
                            LEFT JOIN task_assignment ON C.agent_id = task_assignment.agent_id AND task_assignment.end_date IS NULL
                            ) D
                        LEFT JOIN task ON D.task_id = task.id
                        ORDER BY
                            D.start_date) E
                            GROUP BY
                                E.agent_id
                        ) BB
                    ) F
                ON
                    AA.agent_id = F.agent_id
                WHERE
                    (
                        AA.priority = F.priority OR AA.priority IS NULL OR F.priority IS NULL
                    )
                ) FF
            ) GG
        WHERE
            GG.priority < 1
        ORDER BY
            GG.start_date
        DESC
    LIMIT 1
        ) HH
    LEFT JOIN agent ON HH.agent_id = agent.agent_id;

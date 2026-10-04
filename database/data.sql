-- QuizMaster Seed Data
USE quizmaster_db;

-- 1. Insert Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 2. Insert Categories
INSERT INTO categories (id, name, slug, description, icon, color_code) VALUES
(1, 'Computer Science', 'computer-science', 'Fundamental core principles of computer systems and architecture', 'bi-cpu', '#4f46e5'),
(2, 'Programming', 'programming', 'General programming concepts, paradigms, clean code, and principles', 'bi-code-slash', '#0284c7'),
(3, 'Java', 'java', 'Core Java, OOP, Collections, JVM, Multithreading, and Spring', 'bi-cup-hot', '#ea580c'),
(4, 'Python', 'python', 'Python fundamentals, Data structures, Decorators, and Libraries', 'bi-filetype-py', '#059669'),
(5, 'C/C++', 'c-cpp', 'Pointers, Memory management, Templates, STL, and low-level concepts', 'bi-terminal', '#7c3aed'),
(6, 'Data Structures', 'data-structures', 'Arrays, Linked Lists, Stacks, Queues, Trees, and Graphs', 'bi-diagram-3', '#db2777'),
(7, 'Algorithms', 'algorithms', 'Searching, Sorting, Dynamic Programming, Greedy, and Graph Algorithms', 'bi-lightbulb', '#d97706'),
(8, 'Database Management', 'database-management', 'SQL, Normalization, Relational Algebra, Transactions, and ACID', 'bi-database', '#0d9488'),
(9, 'Computer Networks', 'computer-networks', 'OSI model, TCP/IP, Routing, DNS, HTTP, and Socket programming', 'bi-hdd-network', '#2563eb'),
(10, 'Operating Systems', 'operating-systems', 'Process Scheduling, Deadlocks, Memory Management, and File Systems', 'bi-motherboard', '#4338ca'),
(11, 'Software Engineering', 'software-engineering', 'SDLC, Agile, Scrum, Design Patterns, Testing, and CI/CD', 'bi-kanban', '#16a34a'),
(12, 'Artificial Intelligence', 'artificial-intelligence', 'Search algorithms, Knowledge Representation, Heuristics, and NLP', 'bi-robot', '#9333ea'),
(13, 'Machine Learning', 'machine-learning', 'Supervised/Unsupervised learning, Neural Networks, Metrics, and Model Evaluation', 'bi-graph-up-arrow', '#e11d48'),
(14, 'General Knowledge', 'general-knowledge', 'World affairs, Science breakthroughs, History, and Inventions', 'bi-globe-americas', '#0891b2'),
(15, 'Aptitude', 'aptitude', 'Quantitative Aptitude, Ratios, Speed & Distance, and Percentages', 'bi-calculator', '#ca8a04'),
(16, 'Logical Reasoning', 'logical-reasoning', 'Syllogisms, Blood relations, Number series, Coding-Decoding', 'bi-puzzle', '#65a30d')
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), icon=VALUES(icon), color_code=VALUES(color_code);

-- 3. Insert Achievements
INSERT INTO achievements (id, code, title, description, icon, badge_color, criteria_type, criteria_threshold) VALUES
(1, 'FIRST_QUIZ', 'First Quiz', 'Successfully completed your very first quiz on QuizMaster!', 'bi-stars', '#4f46e5', 'FIRST_QUIZ', 1),
(2, 'QUIZ_MASTER', 'Quiz Master', 'Completed 25 or more quizzes with dedication!', 'bi-award', '#f59e0b', 'QUIZ_COUNT', 25),
(3, 'TEN_COMPLETED', '10 Quizzes Completed', 'Completed 10 quizzes across any category', 'bi-check2-all', '#06b6d4', 'QUIZ_COUNT', 10),
(4, 'HIGH_ACCURACY', '90% Accuracy', 'Achieved 90% or higher accuracy in a single quiz attempt', 'bi-bullseye', '#10b981', 'ACCURACY', 90),
(5, 'SEVEN_DAY_STREAK', '7-Day Streak', 'Maintained a consecutive 7-day quiz learning streak', 'bi-fire', '#ef4444', 'STREAK', 7),
(6, 'PERFECT_SCORE', 'Perfect Score', 'Scored 100% marks in any quiz without any mistakes!', 'bi-trophy-fill', '#8b5cf6', 'PERFECT_SCORE', 100),
(7, 'PROGRAMMING_EXPERT', 'Programming Expert', 'Demonstrated exceptional mastery in Programming & Languages', 'bi-code-square', '#3b82f6', 'CATEGORY_MASTER', 3),
(8, 'DATABASE_EXPERT', 'Database Expert', 'Achieved high proficiency across Database Management topics', 'bi-server', '#14b8a6', 'CATEGORY_MASTER', 8)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- 4. Insert Quizzes
INSERT INTO quizzes (id, title, description, category_id, difficulty, time_limit_minutes, total_marks, pass_percentage, active) VALUES
(1, 'Core Java Fundamentals & OOP', 'Test your knowledge on Java fundamentals, OOP concepts, JVM architecture, and Exception Handling.', 3, 'BEGINNER', 10, 25, 60, TRUE),
(2, 'Python Data Structures & OOP', 'Evaluate your understanding of Python lists, dicts, tuples, list comprehensions, and classes.', 4, 'INTERMEDIATE', 10, 25, 60, TRUE),
(3, 'Data Structures: Arrays, Trees & Graphs', 'Comprehensive quiz covering Linked Lists, Binary Search Trees, Heap, Graph Traversal, and Big-O notation.', 6, 'ADVANCED', 15, 30, 70, TRUE),
(4, 'Database Systems & SQL Mastery', 'Test your understanding of Relational Databases, Normal Forms, Indexing, ACID transactions, and Joins.', 8, 'INTERMEDIATE', 12, 25, 60, TRUE),
(5, 'Computer Networks: Protocols & Architecture', 'Assess knowledge on the OSI 7 layers, TCP/IP handshake, Subnetting, DNS, and HTTP/HTTPS.', 9, 'BEGINNER', 10, 20, 50, TRUE),
(6, 'Operating Systems: Concurrency & Memory', 'Examine concepts in Process Scheduling, Virtual Memory, Paging, Semaphores, and Deadlocks.', 10, 'INTERMEDIATE', 12, 25, 60, TRUE),
(7, 'Machine Learning & AI Foundations', 'Fundamental concepts of Supervised vs Unsupervised learning, Overfitting, Gradient Descent, and Precision/Recall.', 13, 'ADVANCED', 15, 30, 65, TRUE),
(8, 'Quantitative Aptitude & Number Theory', 'Timed quantitative assessment covering percentages, ratios, probability, and time & work.', 15, 'INTERMEDIATE', 12, 25, 50, TRUE)
ON DUPLICATE KEY UPDATE title=VALUES(title), description=VALUES(description);

-- 5. Insert Questions & Options
-- Quiz 1: Java (5 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(1, 1, 'Which of the following is NOT a Java access modifier?', 'MCQ', 'BEGINNER', 'Access Control', 5, 'Java has four access levels: public, protected, private, and default (package-private). "friend" is a keyword in C++, not Java.', 1),
(2, 1, 'What is the size of the int primitive data type in Java?', 'MCQ', 'BEGINNER', 'Data Types', 5, 'In Java, an int is a 32-bit signed two''s complement integer, which equals 4 bytes.', 2),
(3, 1, 'The String class in Java is immutable.', 'TRUE_FALSE', 'BEGINNER', 'Immutability', 5, 'True. Once a String object is created in Java, its state cannot be modified. Any manipulation creates a new String object.', 3),
(4, 1, 'Which component of the JVM is responsible for converting bytecode into native machine instructions at runtime?', 'MCQ', 'INTERMEDIATE', 'JVM Architecture', 5, 'The JIT (Just-In-Time) compiler compiles bytecode to native code during execution to optimize runtime performance.', 4),
(5, 1, 'Select all classes that implement the java.util.List interface:', 'MULTIPLE_ANSWER', 'INTERMEDIATE', 'Collections', 5, 'ArrayList, LinkedList, and Vector implement the List interface. HashSet implements Set, and HashMap implements Map.', 5)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

-- Options for Quiz 1
INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
-- Q1
(1, 'public', FALSE, 1),
(1, 'private', FALSE, 2),
(1, 'friend', TRUE, 3),
(1, 'protected', FALSE, 4),
-- Q2
(2, '2 bytes', FALSE, 1),
(2, '4 bytes (32-bit)', TRUE, 2),
(2, '8 bytes', FALSE, 3),
(2, 'Depends on OS platform', FALSE, 4),
-- Q3
(3, 'True', TRUE, 1),
(3, 'False', FALSE, 2),
-- Q4
(4, 'Class Loader', FALSE, 1),
(4, 'Garbage Collector', FALSE, 2),
(4, 'JIT (Just-In-Time) Compiler', TRUE, 3),
(4, 'Bytecode Verifier', FALSE, 4),
-- Q5 (Multiple answer)
(5, 'java.util.ArrayList', TRUE, 1),
(5, 'java.util.LinkedList', TRUE, 2),
(5, 'java.util.HashSet', FALSE, 3),
(5, 'java.util.Vector', TRUE, 4);

-- Quiz 2: Python (5 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(6, 2, 'Which data structure in Python is mutable and ordered?', 'MCQ', 'BEGINNER', 'Data Structures', 5, 'Lists in Python are ordered and mutable, allowing elements to be added, removed, and changed in place.', 1),
(7, 2, 'What will be the output of: len({1, 2, 2, 3, 4, 4, 4})?', 'MCQ', 'BEGINNER', 'Sets', 5, 'Sets store only unique values. The elements {1, 2, 2, 3, 4, 4, 4} resolve to {1, 2, 3, 4}, with a length of 4.', 2),
(8, 2, 'In Python, functions can be passed as arguments to other functions because they are first-class citizens.', 'TRUE_FALSE', 'INTERMEDIATE', 'Functional Programming', 5, 'True. In Python, functions are first-class objects; they can be assigned to variables, stored in data structures, and passed as arguments.', 3),
(9, 2, 'What is the time complexity of looking up a key in a standard Python dictionary on average?', 'MCQ', 'INTERMEDIATE', 'Dictionaries', 5, 'Python dictionaries are implemented as hash tables, providing O(1) average time complexity for lookups.', 4),
(10, 2, 'Which of the following statements about Python generators are TRUE?', 'MULTIPLE_ANSWER', 'INTERMEDIATE', 'Generators', 5, 'Generators use the yield keyword to produce values lazily on-the-fly and maintain their execution state between iterations.', 5)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

-- Options for Quiz 2
INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
-- Q6
(6, 'Tuple', FALSE, 1),
(6, 'List', TRUE, 2),
(6, 'Set', FALSE, 3),
(6, 'String', FALSE, 4),
-- Q7
(7, '7', FALSE, 1),
(7, '4', TRUE, 2),
(7, '5', FALSE, 3),
(7, 'Error', FALSE, 4),
-- Q8
(8, 'True', TRUE, 1),
(8, 'False', FALSE, 2),
-- Q9
(9, 'O(n)', FALSE, 1),
(9, 'O(log n)', FALSE, 2),
(9, 'O(1)', TRUE, 3),
(9, 'O(n log n)', FALSE, 4),
-- Q10
(10, 'They use the "yield" statement instead of "return"', TRUE, 1),
(10, 'They generate values on-the-fly saving memory', TRUE, 2),
(10, 'They load all items in memory simultaneously', FALSE, 3),
(10, 'They retain their local state between successive calls', TRUE, 4);

-- Quiz 4: Database Systems & SQL (5 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(11, 4, 'Which normal form eliminates transitive functional dependencies?', 'MCQ', 'INTERMEDIATE', 'Normalization', 5, 'Third Normal Form (3NF) requires a relation to be in 2NF and have no non-prime attribute transitively dependent on any candidate key.', 1),
(12, 4, 'What does the "I" in ACID properties stand for?', 'MCQ', 'BEGINNER', 'Transactions', 5, 'ACID stands for Atomicity, Consistency, Isolation, and Durability.', 2),
(13, 4, 'A primary key column can accept multiple NULL values.', 'TRUE_FALSE', 'BEGINNER', 'Keys', 5, 'False. By definition in relational databases, a Primary Key must be unique and cannot contain NULL values.', 3),
(14, 4, 'Which SQL clause is used to filter records resulting from an aggregate function like COUNT() or AVG()?', 'MCQ', 'INTERMEDIATE', 'SQL Queries', 5, 'HAVING is used to filter groups created by GROUP BY after aggregations, whereas WHERE filters individual rows before grouping.', 4),
(15, 4, 'Which of the following are examples of NoSQL databases?', 'MULTIPLE_ANSWER', 'INTERMEDIATE', 'NoSQL', 5, 'MongoDB (Document store) and Cassandra (Wide-column store) are NoSQL databases. PostgreSQL and MySQL are relational SQL databases.', 5)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

-- Options for Quiz 4
INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
-- Q11
(11, '1NF', FALSE, 1),
(11, '2NF', FALSE, 2),
(11, '3NF', TRUE, 3),
(11, 'BCNF', FALSE, 4),
-- Q12
(12, 'Integrity', FALSE, 1),
(12, 'Isolation', TRUE, 2),
(12, 'Indexing', FALSE, 3),
(12, 'Iteration', FALSE, 4),
-- Q13
(13, 'True', FALSE, 1),
(13, 'False', TRUE, 2),
-- Q14
(14, 'WHERE', FALSE, 1),
(14, 'HAVING', TRUE, 2),
(14, 'GROUP BY', FALSE, 3),
(14, 'ORDER BY', FALSE, 4),
-- Q15
(15, 'MongoDB', TRUE, 1),
(15, 'PostgreSQL', FALSE, 2),
(15, 'Apache Cassandra', TRUE, 3),
(15, 'MySQL', FALSE, 4);

-- Quiz 5: Computer Networks (4 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(16, 5, 'Which layer in the OSI reference model is responsible for end-to-end reliability and flow control?', 'MCQ', 'BEGINNER', 'OSI Layers', 5, 'The Transport Layer (Layer 4) ensures end-to-end communication, segmentation, flow control, and error correction (e.g., TCP).', 1),
(17, 5, 'What default port does HTTPS use for secure web traffic?', 'MCQ', 'BEGINNER', 'Ports & Protocols', 5, 'HTTPS uses TCP port 443 by default, whereas unencrypted HTTP uses port 80.', 2),
(18, 5, 'UDP is a connectionless and unreliable protocol compared to TCP.', 'TRUE_FALSE', 'BEGINNER', 'Transport Protocols', 5, 'True. UDP does not establish a handshake, does not guarantee packet delivery or order, but provides lower latency.', 3),
(19, 5, 'Which of the following are private IPv4 address ranges as defined in RFC 1918?', 'MULTIPLE_ANSWER', 'INTERMEDIATE', 'IP Addressing', 5, 'RFC 1918 defines 10.0.0.0/8, 172.16.0.0/12, and 192.168.0.0/16 as private address spaces. 8.8.8.8 is a public DNS address.', 4)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

-- Options for Quiz 5
INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
-- Q16
(16, 'Network Layer', FALSE, 1),
(16, 'Transport Layer', TRUE, 2),
(16, 'Session Layer', FALSE, 3),
(16, 'Data Link Layer', FALSE, 4),
-- Q17
(17, '80', FALSE, 1),
(17, '8080', FALSE, 2),
(17, '443', TRUE, 3),
(17, '22', FALSE, 4),
-- Q18
(18, 'True', TRUE, 1),
(18, 'False', FALSE, 2),
-- Q19
(19, '10.0.0.0 to 10.255.255.255', TRUE, 1),
(19, '192.168.0.0 to 192.168.255.255', TRUE, 2),
(19, '8.8.8.0 to 8.8.8.255', FALSE, 3),
(19, '172.16.0.0 to 172.31.255.255', TRUE, 4);

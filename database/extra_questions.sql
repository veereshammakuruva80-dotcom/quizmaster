USE quizmaster_db;

-- Quiz 3: Data Structures (4 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(20, 3, 'What is the worst-case time complexity of searching for an element in a balanced Binary Search Tree (AVL or Red-Black)?', 'MCQ', 'ADVANCED', 'Trees', 5, 'In a balanced binary search tree with n nodes, the height is guaranteed to be O(log n), so search takes O(log n) worst-case time.', 1),
(21, 3, 'A Stack data structure operates on which principle?', 'MCQ', 'BEGINNER', 'Stacks', 5, 'A Stack follows the Last-In-First-Out (LIFO) order where the last inserted element is removed first.', 2),
(22, 3, 'In a max-heap, the value of the root node is always greater than or equal to all its descendants.', 'TRUE_FALSE', 'INTERMEDIATE', 'Heaps', 5, 'True. The max-heap property dictates that each parent node has a value greater than or equal to its children.', 3),
(23, 3, 'Which of the following data structures can be used to implement a Priority Queue efficiently?', 'MULTIPLE_ANSWER', 'ADVANCED', 'Priority Queues', 5, 'Binary Heap and Fibonacci Heap provide efficient implementations of priority queues. A singly linked list has O(n) insertion/extraction.', 4)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
(20, 'O(1)', FALSE, 1),
(20, 'O(n)', FALSE, 2),
(20, 'O(log n)', TRUE, 3),
(20, 'O(n log n)', FALSE, 4),
(21, 'FIFO (First-In-First-Out)', FALSE, 1),
(21, 'LIFO (Last-In-First-Out)', TRUE, 2),
(21, 'LILO (Last-In-Last-Out)', FALSE, 3),
(21, 'Priority Ordering only', FALSE, 4),
(22, 'True', TRUE, 1),
(22, 'False', FALSE, 2),
(23, 'Binary Heap', TRUE, 1),
(23, 'Fibonacci Heap', TRUE, 2),
(23, 'Unsorted Array', FALSE, 3),
(23, 'Binomial Heap', TRUE, 4);

-- Quiz 6: Operating Systems (4 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(24, 6, 'Which condition is NOT one of the four Coffman conditions necessary for a Deadlock to occur?', 'MCQ', 'INTERMEDIATE', 'Deadlocks', 5, 'The four Coffman conditions are Mutual Exclusion, Hold and Wait, No Preemption, and Circular Wait. "Preemptive scheduling" prevents deadlock.', 1),
(25, 6, 'Thrashing occurs when the operating system spends more time paging than executing processes.', 'TRUE_FALSE', 'INTERMEDIATE', 'Virtual Memory', 5, 'True. Thrashing happens when the working set of active processes exceeds physical memory, causing constant page faults.', 2),
(26, 6, 'Which CPU scheduling algorithm may lead to starvation for long processes?', 'MCQ', 'INTERMEDIATE', 'CPU Scheduling', 5, 'Shortest Job First (SJF) and Shortest Remaining Time First (SRTF) prioritize short jobs, leaving long jobs to starve.', 3),
(27, 6, 'Select all IPC (Inter-Process Communication) mechanisms provided by modern OS:', 'MULTIPLE_ANSWER', 'INTERMEDIATE', 'IPC', 5, 'Pipes, Shared Memory, Message Queues, and Sockets are standard IPC mechanisms.', 4)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
(24, 'Mutual Exclusion', FALSE, 1),
(24, 'Circular Wait', FALSE, 2),
(24, 'Preemptive Scheduling', TRUE, 3),
(24, 'Hold and Wait', FALSE, 4),
(25, 'True', TRUE, 1),
(25, 'False', FALSE, 2),
(26, 'Round Robin', FALSE, 1),
(26, 'First-Come First-Served', FALSE, 2),
(26, 'Shortest Job First (SJF)', TRUE, 3),
(26, 'Multilevel Feedback Queue', FALSE, 4),
(27, 'Shared Memory', TRUE, 1),
(27, 'Named Pipes / FIFOs', TRUE, 2),
(27, 'Message Queues', TRUE, 3),
(27, 'Register Renaming', FALSE, 4);

-- Quiz 7: Machine Learning (4 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(28, 7, 'Which metric is best suited to evaluate a classification model on an imbalanced dataset?', 'MCQ', 'ADVANCED', 'Model Evaluation', 5, 'F1-Score (or PR-AUC) balances precision and recall, making it reliable for imbalanced classes where accuracy is misleading.', 1),
(29, 7, 'Overfitting occurs when a model performs extremely well on training data but poorly on unseen test data.', 'TRUE_FALSE', 'BEGINNER', 'Generalization', 5, 'True. High variance / overfitting means the model learned the noise in the training set.', 2),
(30, 7, 'What is the role of L2 regularization (Ridge) in linear and logistic regression?', 'MCQ', 'ADVANCED', 'Regularization', 5, 'L2 regularization penalizes the sum of squared weights, shrinking them toward zero to prevent overfitting.', 3),
(31, 7, 'Which of the following are unsupervised learning algorithms?', 'MULTIPLE_ANSWER', 'INTERMEDIATE', 'Unsupervised Learning', 5, 'K-Means clustering and PCA (Principal Component Analysis) are unsupervised. Logistic Regression and Random Forest are supervised.', 4)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
(28, 'Accuracy', FALSE, 1),
(28, 'F1-Score / PR-AUC', TRUE, 2),
(28, 'Mean Squared Error', FALSE, 3),
(28, 'Adjusted R-squared', FALSE, 4),
(29, 'True', TRUE, 1),
(29, 'False', FALSE, 2),
(30, 'It performs feature selection by zeroing weights', FALSE, 1),
(30, 'It shrinks weights toward zero proportionally', TRUE, 2),
(30, 'It increases model complexity', FALSE, 3),
(30, 'It computes matrix inverse without iterations', FALSE, 4),
(31, 'K-Means Clustering', TRUE, 1),
(31, 'Principal Component Analysis (PCA)', TRUE, 2),
(31, 'Linear Regression', FALSE, 3),
(31, 'Support Vector Classifier', FALSE, 4);

-- Quiz 8: Quantitative Aptitude (4 Questions)
INSERT INTO questions (id, quiz_id, question_text, question_type, difficulty, topic, marks, explanation, sort_order) VALUES
(32, 8, 'If a train traveling at 72 km/h crosses a 200m platform in 20 seconds, what is the length of the train?', 'MCQ', 'INTERMEDIATE', 'Speed & Distance', 5, 'Speed = 72 * (5/18) = 20 m/s. Total distance in 20s = 20 * 20 = 400m. Train length = 400 - 200 = 200m.', 1),
(33, 8, 'The probability of rolling a sum of 7 with two standard 6-sided dice is 1/6.', 'TRUE_FALSE', 'BEGINNER', 'Probability', 5, 'True. There are 6 combinations summing to 7: (1,6),(2,5),(3,4),(4,3),(5,2),(6,1) out of 36 possibilities = 6/36 = 1/6.', 2),
(34, 8, 'A can finish a work in 12 days and B can finish in 24 days. How many days will they take working together?', 'MCQ', 'INTERMEDIATE', 'Time & Work', 5, 'Combined daily work = 1/12 + 1/24 = 3/24 = 1/8. So together they take 8 days.', 3),
(35, 8, 'If the price of an item is increased by 20% and then reduced by 20%, what is the net percentage change?', 'MCQ', 'BEGINNER', 'Percentages', 5, 'Let initial price = 100. After +20% -> 120. After -20% of 120 -> 120 - 24 = 96. Net change = 4% decrease.', 4)
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

INSERT INTO question_options (question_id, option_text, is_correct, sort_order) VALUES
(32, '150 m', FALSE, 1),
(32, '200 m', TRUE, 2),
(32, '250 m', FALSE, 3),
(32, '180 m', FALSE, 4),
(33, 'True', TRUE, 1),
(33, 'False', FALSE, 2),
(34, '6 days', FALSE, 1),
(34, '8 days', TRUE, 2),
(34, '10 days', FALSE, 3),
(34, '7.5 days', FALSE, 4),
(35, 'No change (0%)', FALSE, 1),
(35, '4% decrease', TRUE, 2),
(35, '2% decrease', FALSE, 3),
(35, '4% increase', FALSE, 4);

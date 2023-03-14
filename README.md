# A-Simple-Text-Based-Search-Engine

As you know, most of the information you need is provided by internet search engines. One of the most preferred search engines is still Google. The fundamental factors behind the success of Google are quickness and correctness. How does Google retrieve quick and correct results for user queries? In this assignment, you will investigate the answers of this question. You will develop a simple text-based search engine. Your program only operates on text files that are kept in a single directory. We will provide necessary text documents into our document database.
In this assignment, you are expected to implement a hashing algorithm in Java to index words of documents given in X. You must read these documents, split it word by word, and index each word to your hash table according to rules given below. After insertion of all documents, the user will query for a sentence that contains 3 words splitted by a single space. Your algorithm must bring most relevant document for the query written by the user.
Rules:
- Object Oriented Principles (OOP) and try-catch exception handling must be used when it is needed.
- Hash function for converting a word to a key must be implemented by yourself. The value will be the word and the key will be returned by your hash function. The number of occurrences of each word also must be stored as count value for each document separately. 

- You should write a relevance measure to prioritize the most relevant document. After calculating distances of the resulting documents to the given query, you should sort documents according to these distance values, i.e., the smaller distance gets a higher rank in the resulting list.

Main Steps:
1-	Remove punctuation and stop words.
2-	Create necessary data structures (Hash Table) and insertion.
3-	Search the given sentence and find the most relevant document.

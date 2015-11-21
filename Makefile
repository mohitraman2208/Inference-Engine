agent: Solution.java IOHandler.java InferenceEngine.java AtomicSentence.java KnowledgeBase.java PredicateTable.java Sentence.java Substitutions.java

Solution.java: IOHandler.java InferenceEngine.java
			javac -classpath . com/ai/homework3/Solution.java

IOHandler.java: InferenceEngine.java
			javac -classpath . com/ai/homework3/io/IOHandler.java

InferenceEngine.java: KnowledgeBase.java Sentence.java AtomicSentence.java Substitutions.java
			javac -classpath . com/ai/inference/InferenceEngine.java
KnowledgeBase.java: AtomicSentence.java Sentence.java Substitutions.java PredicateTable.java
			javac -classpath . com/ai/inference/kb/KnowledgeBase.java
AtomicSentence.java: Substitutions.java
			javac -classpath . com/ai/inference/kb/AtomicSentence.java
PredicateTable.java: AtomicSentence.java Sentence.java
			javac -classpath . com/ai/inference/kb/PredicateTable.java
Sentence.java: AtomicSentence.java Substitutions.java
			javac -classpath . com/ai/inference/kb/Sentence.java
Substitutions.java:
			javac -classpath . com/ai/inference/kb/Substitutions.java

run: Solution.class

Solution.class: 
			java -classpath . com.ai.homework3.Solution
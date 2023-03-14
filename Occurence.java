package google_test1;

public class Occurence {
	//this class holds the word,it's document and it occurence frequency in that document.claxt
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	String word;
	String document;
	int frequency;
	
	public Occurence(String st,String doc, int freq)
	{
		word=st;
		document=doc;
		frequency=freq;
	}

}



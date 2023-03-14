package google_test1;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashedDictionary<K, V> implements DictionaryInterface<K, V> {
	private TableEntry<K, V>[] hashTable; 
	private int numberOfEntries;
	private int locationsUsed; 
	private static final int DEFAULT_SIZE = 101; 
	private static final double MAX_LOAD_FACTOR = 0.8;

	public static int getFakeAscii(char a)
	{
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		int ascii=alpha.indexOf(a);
		ascii=ascii+1;
		return ascii;
		
	}
	public int getHashFunction(K key)
	{
		String word=key.toString();

		int hash=0;
		int z=31;
		for(int i=0; i<word.length();i++)
		{
			hash=hash+(getFakeAscii(word.charAt(word.length()-1-i))*(int)Math.pow(z,i));//PAF
			//hash=hash+(getFakeAscii(word.charAt(word.length()-1-i)));//SSF
		}
		return hash;
		
	}
	
	
	
	
	
	public HashedDictionary() {
		this(DEFAULT_SIZE); 
	} 

	@SuppressWarnings("unchecked")
	public HashedDictionary(int tableSize) {
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntry[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
	}

	public boolean isPrime(int num) {
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++) {
			if ((num % i) == 0) {
				prime = false;
				break;
			}
		}
		return prime;
	}

	public int getNextPrime(int num) {
		if (num <= 1)
            return 2;
		else if(isPrime(num))
			return num;
        boolean found = false;   
        while (!found)
        {
            num++;     
            if (isPrime(num))
                found = true;
        }     
        return num;
	}

	public V add(K key, V value) {
		V oldValue; 
		if (isHashTableTooFull())
			rehash();
		int wordKey=getHashFunction(key);
		int index = getHashIndex(wordKey);
		index = probe(index, key); 

		if ((hashTable[index] == null) || hashTable[index].isRemoved()) { 
			hashTable[index] = new TableEntry<K, V>(key, value);
			numberOfEntries++;
			locationsUsed++;
			oldValue = null;
		} else { 
			oldValue = hashTable[index].getValue();
			hashTable[index].setValue(value);
		} 
		return oldValue;
	}

	private int getHashIndex(int wordKey) {
		int hashIndex = wordKey % hashTable.length;
		if (hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}

	public boolean isHashTableTooFull() {
		int load_factor = locationsUsed / hashTable.length;
		if (load_factor >= MAX_LOAD_FACTOR)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public void rehash() {
		TableEntry<K, V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(2 * oldSize);
		hashTable = new TableEntry[newSize]; 
		numberOfEntries = 0; 
		locationsUsed = 0;

		for (int index = 0; index < oldSize; index++) 
		{
			if(oldTable[index]!=null&&oldTable[index].isIn())
				 add(oldTable[index].getKey(),oldTable[index].getValue());
		
		}
	}

	private int probe(int index, K key) {
		boolean found = false;
		int removedStateIndex = -1; 
		int i=0;
		int h2=31-(getHashFunction(key)%31);
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) 
			{
				if (key.equals(hashTable[index].getKey()))
					found = true; 
				else 
				{	
					//index = (index + 1) % hashTable.length; //LP
			        
				    i=i+1;				    
				    index=(index+(i*h2))%hashTable.length; //DH
				    if(index<0)
				    {
				       index = index + hashTable.length;
				    }
				}
			} 
		
			else 
			{
				
				if (removedStateIndex == -1)
					removedStateIndex = index;
			// index = (index + 1) % hashTable.length; //LP
				i=i+1;
				index=(index+(i*h2))%hashTable.length;//DH
			    if(index<0)
			    {
			       index = index + hashTable.length;
			    }
			} 
		} 
		if (found || (removedStateIndex == -1))
			return index; 
		else
			return removedStateIndex; 
	}

	public V remove(K key) {
		V removedValue = null;
		int wordKey=getHashFunction(key);
	    int index=getHashIndex(wordKey);
	    index=locate(index,key).index;
		if (index != -1) { 
			removedValue = hashTable[index].getValue();
			hashTable[index].setToRemoved();
			numberOfEntries--;
		} 
		return removedValue;
	}
    
    //this returns index of the key and how many collisions occured while locating(finding/searching) the key.
	public CollusionAndIndex locate(int index, K key) {
		boolean found = false;
		int col=0;
		CollusionAndIndex col_inx=new CollusionAndIndex(col,index);
		int h2=31-(getHashFunction(key)%31);
		int i=0;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true; 
			else 
			{
				//index = (index + 1) % hashTable.length; //LP
		//col_inx.collusion_count++;              
				i=i+1;				
				index=(index+(i*h2))%hashTable.length;//DH
			    col_inx.collusion_count++; //everytime we look for another index means a collusion so we increase it by 1.
			    if(index<0)
			    {
			       index = index + hashTable.length;
			    }
			}
			
		} 
		int result = -1;
		if (found)
			
		{
			result = index;

		}		
		col_inx.index=result;
		return col_inx;
	}

	public V getValue(K key) {
		V result = null;
		int wordKey=getHashFunction(key);
		int index = getHashIndex(wordKey);
		index = locate(index, key).index;
		if (index != -1)
			result = hashTable[index].getValue(); 
		return result;
	}

	//this returns if hashtable contains the key, and if so how many collusion do we get while searching it.
	public ContainAndCollusion contains(K key) {
		int wordKey=getHashFunction(key);
		int index = getHashIndex(wordKey);
		CollusionAndIndex calculate=locate(index, key);
		index =calculate.index;
		int collusion=calculate.collusion_count;
		ContainAndCollusion result =new ContainAndCollusion(false,collusion);
		result.collusion=collusion;
		
		if (index != -1)
		{
			result.contain=true;
			return result;
		}
		else
		{
			result.contain=false;
			return result;
		}
		
	}

	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public int getSize() {
		return numberOfEntries;
	}

	public void clear() {
		while(getKeyIterator().hasNext()) {
			remove(getKeyIterator().next());		
		}
	}
	
	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	private class TableEntry<S, T> {
		private S key;
		private T value;
		private boolean inTable;

		private TableEntry(S key, T value) {
			this.key = key;
			this.value = value;
			inTable = true;
		}

		
		private S getKey() {
			return key;
		}

		private T getValue() {
			return value;
		}

		private void setValue(T value) {
			this.value = value;
		}

		private boolean isRemoved() {
			return inTable == false;
		}

		private void setToRemoved() {
			inTable = false;
		}

		private void setToIn() {
			inTable = true;
		}

		private boolean isIn() {
			return inTable == true;
		}
	}

	private class KeyIterator implements Iterator<K> {
		private int currentIndex; 
		private int numberLeft; 

		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		} 

		public boolean hasNext() {
			return numberLeft > 0;
		} 

		public K next() {
			K result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				} 
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		} 

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
	
	public class CollusionAndIndex {

		
		int collusion_count;
		int index;
		public CollusionAndIndex(int coll,int i)
		{
			
			collusion_count=coll;
			index=i;
		}
	}
	public class ContainAndCollusion {

		
		boolean contain;
		int collusion;
		public ContainAndCollusion(boolean cont,int col)
		{
			
			contain=cont;
			collusion=col;
		}
	}
	private class ValueIterator implements Iterator<V> {
		private int currentIndex; 
		private int numberLeft; 

		private ValueIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		} 

		public boolean hasNext() {
			return numberLeft > 0;
		} 

		public V next() {
			V result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				} 
				result = hashTable[currentIndex].getValue();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		} 

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
}

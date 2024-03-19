package Dictionary;
import java.util.ArrayList;
import java.util.Hashtable;

import List.ListInterface;
import List.MyLinkedList;

public class MyHashtable implements DictionaryInterface {

    protected int tableSize;
    protected int size;
    // The LinkedList is of type Entry
    protected MyLinkedList[] table;

    public MyHashtable(int tableSize) {
        table = new MyLinkedList[tableSize];
        this.tableSize = tableSize;
    }

    public int biggestBucket()
    {
        int biggestBucket = 0;
        for(int i = 0; i < table.length; i++) {
            // Loop through the table looking for non-null locations.
            if (table[i] != null) {
                // If you find a non-null location, compare the bucket size against the largest
                // bucket size found so far. If the current bucket size is bigger, set biggestBucket
                // to this new size.
                MyLinkedList bucket = table[i];
                if (biggestBucket < bucket.size())
                    biggestBucket = bucket.size();
            }
        }
        return biggestBucket; // Return the size of the biggest bucket found.
    }

    // Returns the average bucket length. Gives a sense of how many collisions are happening overall.
    public float averageBucket() {
        float bucketCount = 0; // Number of buckets (non-null table locations)
        float bucketSizeSum = 0; // Sum of the size of all buckets
        for(int i = 0; i < table.length; i++) {
            // Loop through the table
            if (table[i] != null) {
                // For a non-null location, increment the bucketCount and add to the bucketSizeSum
                MyLinkedList bucket = table[i];
                bucketSizeSum += bucket.size();
                bucketCount++;
            }
        }

        // Divide bucketSizeSum by the number of buckets to get an average bucket length.
        return bucketSizeSum/bucketCount;
    }

    public String toString()
    {
        String s = "";
        for(int tableIndex = 0; tableIndex < tableSize; tableIndex++) {
            if (table[tableIndex] != null) {
                MyLinkedList bucket = table[tableIndex];
                for(int listIndex = 0; listIndex < bucket.size(); listIndex++) {
                    Entry e = (Entry)bucket.get(listIndex);
                    s = s + "key: " + e.key + ", value: " + e.value + "\n";
                }
            }
        }
        return s;
    }

    protected class Entry
    {
        String key;
        Object value;

        Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    // Implement these methods
    public boolean isEmpty() {return size == 0;} // returns true if the Dictionary is empty, false otherwise.

    public int size(){return size;} //Returns the number of key/value pairs stored in the dictionary.

    // Adds a value stored under the given key. If the key has already been stored in the Dictionary,
    // replaces the value associated with the key and returns the old value. If the key isn't in the dictionary
    // returns null.
    public Object put(String key, Object value){
        // 1. Compute an array index given the key
        int index = Math.abs(key.hashCode()) % tableSize;
        // 2. If that location in the table is null,
        // that means nothing has been previously stored using a key with this hash code.
        if (table[index] == null) {
            // a. Create a new MyLinkedList to be the bucket.
            table[index] = new MyLinkedList();
            // b. Add the new Entry for the key/value pair to the list.
            table[index].add(0, new Entry(key, value));
            // c. Set this location in the array equal to the new bucket (list).
            // d. Increment the size (the number of unique keys you have stored).
             size++;
        }
        // 3. If the location in the table isn't null,
        // that means keys with this colliding hash code have been previously stored.
        else { 
        // 3a, a value exists for the key
        MyLinkedList bucket = table[index];
            // a. Linearly search through the bucket (the list) stored at this array
            // location comparing the key for each entry with the key passed into put().
            for (int i = 0; i < bucket.size(); i++) {
		Entry e = (Entry) bucket.get(i);
		if (e.key.equals(key)) {
			Object oldValue = e.value;
			e.value = value;
			return oldValue;
				}
			}
                // If you get a match, this means this key as been previously stored.
                    // Save the old value in the Entry (so you can return it) and replace it with the new value.
                    // (use the code below)
                 //    Entry oldValue = new Entry(key, e.value);
                   //   e.value = value;
                    // NOTE: this is technically not correct as you would need to create a deep copy of the entry,
                    // however, that is outside the realm of this assignment. The code above will be
                    // enough to complete the assignment
                   // return old value here
                  //   return oldValue.value;
        // 3b, a value does not exist for the key
             
            // b. If you don't find the key in the bucket,
            // then just add a new Entry (with the key and value) to the beginning of the list.
             bucket.add(0, new Entry(key, value));
            // Increment the size.
          size++;    
	}
        return null;
}
    public Object get(String key){
        // 1. Compute an array index given the key
       int index = Math.abs(key.hashCode()) % tableSize;
        // 2. If that location in the table is null,
        // that means nothing has been stored using a key with this hash code.
        // So we can return null.
        if (table[index] == null) {
			return null;
        }
	else{
        // 4. Linearly search through the bucket (the list),
        // comparing the key for each entry with the key passed into get().
        MyLinkedList bucket = table[index];
            // Extracting each element in
            // the Linked List
         for (int i = 0; i < bucket.size(); i++) {
	Entry e = (Entry) bucket.get(i);
	    if (e.key.equals(key)) {
            // If you find a match, return the value.
		return e.value;
	    }
	 }
        // If the key is not found in the bucket, return null.
        return null;
	}
    } // Retrieves the value stored with the key.

    public void remove(String key){
        // 1. Compute an array index given the key
    int index = Math.abs(key.hashCode()) % tableSize;
        // 2. If that location in the table is null, then this key has definitely not been used to store a value.
        if (table[index] != null) {	   
 
        // 3. If the location in the table has a bucket,
        // we need to linearly search it to see if it contains an Entry with the key.
        MyLinkedList bucket = table[index];
		for (int i = 0; i < bucket.size(); i++) {
			Entry e = (Entry) bucket.get(i);
            // If you find an Entry in the bucket (linked list) with the key:
            if (e.key.equals(key)) {
                // a. Remove this Entry from the bucket.
                bucket.remove(i);
                // b. Decrement size (the number of unique keys stored in the hashtable).
                size--;
                return;
	    }
            }
        }
    } // Deletes the key/value pair stored with the given key.

    public void clear(){
	     for (int i = 0; i < table.length; i++) {
        table[i] = null;
    }
    size = 0;
    } // Empties the dictionary.

    public String[] getKeys(){
        // 1. Create a String[] with a size equal to the number of unique keys in the hashtable
        String[] keys = new String[size];
        // 2. Iterate through the hashtable array.
		int currentIndex = 0;
		for (int i = 0; i < table.length; i++) {
            // For each table location that isn't null
            MyLinkedList bucket = table[i];
			if (bucket != null) {
                // a. Iterate though the bucket (linked list)
		     for (int j = 0; j < bucket.size(); j++) {
                    // getting the key out of each Entry and storing it in
                    // the array of strings you created in step 1.
			Entry e = (Entry) bucket.get(j);
                        keys[index] = e.key;
                        index++;
				}
			}
		}
        // 3. Return the String[]
        return keys;
    }

}

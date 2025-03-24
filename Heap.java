public class Heap {
    private final int[] items = new int[10];
    private int size;

    public void insert(int value){
        if(isFull())
            throw new IllegalStateException("Heap is full");

        items[size++] = value;

        bubbleUp();
    }

    public void remove(){
        if (isEmpty())
            throw new IllegalStateException("Heap is empty");

        items[0] = items[--size];

        bubbleDown();
    }

    private int largerChildIndex(int index){
        return (leftChild(index) > rightChild(index)) ?
                leftChildIndex(index) : rightChildIndex(index);
    }

    private boolean isValidParent(int index){
        return items[index] >= leftChild(index)&&
                items[index] >= rightChild(index);
    }

    private int leftChild(int index){
        return items[leftChildIndex(index)];
    }

    private int rightChild(int index){
        return items[rightChildIndex(index)];
    }

    private int leftChildIndex(int index){
        return index * 2 + 1;
    }

    private int rightChildIndex(int index){
        return index * 2 + 2;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public boolean isFull(){
        return size == items.length;
    }

    private void bubbleDown(){
        var index = 0;
        while (index <= size && !isValidParent(index)){
            var largerChildIndex = largerChildIndex(index);
            swap(index, largerChildIndex);
            index = largerChildIndex;
        }
    }

    private void bubbleUp(){
        int index = size - 1;
        while (index > 0 && items[index] > items[parent(index)]){
            swap(index, parent(index));
            index = parent(index);
        }
    }

    private void swap(int first, int second){
        var temp = items[first];
        items[first] = items[second];
        items[second] = temp;
    }

    private int parent(int index){
        return (index - 1) / 2;
    }
}

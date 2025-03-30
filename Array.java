public class Array {
    private int[] items;
    private int count;

    public Array(int length) {
        items = new int[length];
    }

    private void resizeIfRequired() {
        if (count == items.length) {
            int[] newItems = new int[items.length * 2];

            for (int i = 0; i < count; i++)
                newItems[i] = items[i];

            items = newItems;
        }
    }

    public void insert(int item) {
        resizeIfRequired();

        items[count++] = item;
    }

    public void insertAt(int item, int index) {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException();

        resizeIfRequired();

        for (int i = count -1; i >= index; i--)
            items[i + 1] = items[i];

        items[index] = item;
        count++;
    }

    public void reverse(){
        int[] temp = new int[count];

        int j = 0;
        for (int i = count - 1; i >= 0; i--)
            temp[j++] = items[i];

        items = temp;
    }

    public int max(){
        int max = 0;
        for (var item : items)
            max = Math.max(max, item);

        return max;
    }

    public Array intersection(Array other) {
        var intersection = new Array(count);

        for (var item : items)
            if (other.indexOf(item) >= 0)
                intersection.insert(item);

        return intersection;
    }

    public int indexOf(int item) {
        for (int i = 0; i < count; i++)
            if (items[i] == item)
                return i;

        return -1;
    }

    public void removeAt(int index) {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException();

        for (int i = index; i < count; i++)
            items[i] = items[i + 1];

        count--;
    }

    public void print() {
        for (var i = 0; i < count; i++)
            System.out.println(items[i]);
    }
}

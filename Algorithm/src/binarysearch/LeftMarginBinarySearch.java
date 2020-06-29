package binarysearch;

public class LeftMarginBinarySearch {
    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 6, 6, 7, 8, 9, 13, 14};

        int left = 0;
        int right = array.length;

        int target = 4;

        while (left < right) {
            int mid = (left + right) >> 1;
            if (array[mid] == target) {
                right = mid;
            } else if (array[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        if (left == array.length) {
            System.out.println("not find target value");
        } else {
            System.out.println(array[left] == target ? left : "not find target value");
        }
    }
}

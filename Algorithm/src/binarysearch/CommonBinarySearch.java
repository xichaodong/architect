package binarysearch;

/**
 * @author chaodong.xi
 * @date 2020/11/13 下午6:23
 */
public class CommonBinarySearch {
    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 3, 3, 3, 3, 3, 3, 4, 5, 6, 7, 8, 9};
        int target = 3;

        System.out.println(binarySearch(target, array));
    }

    private static int binarySearch(int target, int[] array) {
        int left = 0;
        int right = array.length;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] == target) {
                return mid;
            } else if (array[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return -1;
    }
}

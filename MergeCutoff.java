public class MergeCutoff {

    // Hàm so sánh chuẩn sách Princeton
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // Hàm đổi chỗ 2 phần tử chuẩn sách Princeton
    @SuppressWarnings("rawtypes")
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // Insertion Sort cho đoạn mảng con a[lo..hi]
    @SuppressWarnings("rawtypes")
    private static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1]); j--) {
                exch(a, j, j - 1);
            }
        }
    }

    // Hàm merge chuẩn trong slide bài giảng (phần /* as before */)
    @SuppressWarnings("rawtypes")
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        // 1. Copy từ mảng a sang mảng phụ aux
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // 2. Trộn 2 nửa từ aux ngược trở lại vào a
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }
    }

    // Hàm sort đệ quy từ slide của bạn (đã nâng cấp thêm CUTOFF)
    @SuppressWarnings("rawtypes")
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, int cutoff) {
        // Thay vì "if (hi <= lo) return;", ta kiểm tra độ dài đoạn con so với cutoff:
        if (hi <= lo + cutoff - 1) {
            insertionSort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, cutoff);
        sort(a, aux, mid + 1, hi, cutoff);
        merge(a, aux, lo, mid, hi);
    }

    // Hàm sort chính để gọi từ bên ngoài
    @SuppressWarnings("rawtypes")
    public static void sort(Comparable[] a, int cutoff) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1, cutoff);
    }
}
import pandas as pd
import matplotlib.pyplot as plt

# 1. Đọc dữ liệu từ file ket_qua.csv vừa chạy ra
df = pd.read_csv("ket_qua.csv")

plt.figure(figsize=(10, 6))

# 2. Vẽ đường thời gian trung bình kèm thanh độ lệch chuẩn (Error Bars)
plt.errorbar(
    df["Cutoff"], df["MeanTimeMs"], yerr=df["StdDevMs"],
    fmt='-o', color='#1f77b4', ecolor='#aec7e8', elinewidth=1.5, capsize=4,
    label="Thời gian chạy trung bình ± Độ lệch chuẩn (ms)"
)

# 3. Tô màu vùng CUTOFF 7 - 10 mà sách Princeton đề cập
plt.axvspan(7, 10, color='orange', alpha=0.2, label="Vùng giáo trình gợi ý (CUTOFF 7 - 10)")

# 4. Tự động tìm và đánh dấu ngôi sao đỏ tại CUTOFF nhanh nhất trên máy của bạn
best_idx = df["MeanTimeMs"].idxmin()
best_cutoff = int(df.loc[best_idx, "Cutoff"])
best_time = df.loc[best_idx, "MeanTimeMs"]

plt.scatter(best_cutoff, best_time, color='red', s=120, zorder=5,
            label=f"Tối ưu trên máy này: CUTOFF = {best_cutoff} ({best_time:.2f} ms)")

# 5. Trang trí biểu đồ
plt.title("Thực nghiệm tìm giá trị CUTOFF tối ưu cho Mergesort (N = 500,000)", fontsize=13, fontweight='bold')
plt.xlabel("Giá trị CUTOFF (Độ dài mảng con dùng Insertion Sort)", fontsize=11)
plt.ylabel("Thời gian chạy trung bình (ms)", fontsize=11)
plt.xticks(range(1, 31, 1))
plt.grid(True, linestyle='--', alpha=0.6)
plt.legend(fontsize=10)
plt.tight_layout()

# 6. Lưu ra file ảnh nét cao để chèn vào Word
plt.savefig("do_thi_cutoff.png", dpi=300)
print(f"Đã lưu biểu đồ thành công vào file 'do_thi_cutoff.png'! CUTOFF tốt nhất là {best_cutoff}.")
plt.show()
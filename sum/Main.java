import java.io.*;
import java.util.*;

public class Main {
    static final int MOD = 998244353;

    public static void main(String[] args) throws IOException {
        // 使用快速输入，防止 500000 的数据量导致 Scanner 超时
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        int[] a = new int[n + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(st.nextToken());
        }

        int[] left = new int[n + 2];  // 记录左边第一个比 a[i] 大的位置
        int[] right = new int[n + 2]; // 记录右边第一个大于等于 a[i] 的位置
        
        // 手动实现数组栈，比 Java 的 Stack 类快很多
        int[] stack = new int[n + 2];
        int top = 0;

        // 1. 从左向右找，维护一个单调递减栈
        for (int i = 1; i <= n; i++) {
            while (top > 0 && a[stack[top]] <= a[i]) {
                top--;
            }
            left[i] = (top == 0) ? 0 : stack[top];
            stack[++top] = i;
        }

        // 2. 从右向左找，维护一个单调递减栈
        top = 0;
        for (int i = n; i >= 1; i--) {
            while (top > 0 && a[stack[top]] < a[i]) { // 注意这里是严格小于，避免重复计算
                top--;
            }
            right[i] = (top == 0) ? n + 1 : stack[top];
            stack[++top] = i;
        }

        long ans = 0;

        // 3. 计算每个元素的贡献
        for (int i = 1; i <= n; i++) {
            long L = i - left[i];       // 向左可以扩展的最大长度
            long R = right[i] - i;      // 向右可以扩展的最大长度
            
            // 计算位数总和
            long sumOfBits = calcSum(L, R);
            
            // 当前元素的贡献 = 当前值 * 包含它的所有合法区间的位数总和
            long contribution = (long) a[i] * sumOfBits % MOD;
            ans = (ans + contribution) % MOD;
        }

        System.out.println(ans);
    }

    /**
     * 核心数学公式：计算长度为 1 到 L+R-1 的所有包含当前元素的子区间的“长度位数”之和
     * L: 向左扩展的最大长度，R: 向右扩展的最大长度
     */
    static long calcSum(long L, long R) {
        long maxLen = L + R - 1; // 包含当前元素的最大区间长度
        long A = Math.min(L, R);
        long B = Math.max(L, R);
        
        long total = 0;
        long p10 = 1;

        // 因为 n <= 500000，位数最多 6 位，循环最多 6 次
        for (int d = 1; d <= 6; d++) {
            long k1 = p10;                // 当前位数区间的下界 (10^{d-1})
            long k2 = p10 * 10 - 1;       // 当前位数区间的上界 (10^d - 1)
            
            if (k1 > maxLen) break; // 如果超过最大长度了，后面的不用算了
            
            long start = k1;
            long end = Math.min(k2, maxLen);
            
            long sumC = 0; // 用于累加这个位数区间内的组合数

            // 根据组合数学，区间长度 k 与出现次数的关系是一个梯形函数，分三段计算：
            
            // 第一段：上升期 [1, A] -> 次数为 k
            long s1 = start, e1 = Math.min(end, A);
            if (s1 <= e1) {
                sumC = (sumC + getRangeSum(s1, e1)) % MOD;
            }
            
            // 第二段：平顶期 [A+1, B] -> 次数为 A
            long s2 = Math.max(start, A + 1), e2 = Math.min(end, B);
            if (s2 <= e2) {
                long count = e2 - s2 + 1;
                sumC = (sumC + (A % MOD) * (count % MOD)) % MOD;
            }
            
            // 第三段：下降期 [B+1, A+B-1] -> 次数为 (A+B-k)
            long s3 = Math.max(start, B + 1), e3 = Math.min(end, A + B - 1);
            if (s3 <= e3) {
                long count = e3 - s3 + 1;
                long part1 = ((A + B) % MOD) * (count % MOD) % MOD;
                long part2 = getRangeSum(s3, e3);
                sumC = (sumC + part1 - part2 + MOD) % MOD; // 防负数取模
            }
            
            // 将次数乘以当前的位数 d
            total = (total + (sumC % MOD) * d) % MOD;
            p10 *= 10;
        }
        return total;
    }

    // 等差数列求和公式：求 [s, e] 之间整数的和
    static long getRangeSum(long s, long e) {
        long count = e - s + 1;
        long sum = (s + e) * count / 2;
        return sum % MOD;
    }
}
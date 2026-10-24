import java.io.*;
import java.util.*;

public class NonCoprimeGame {
    static final int MAXA = 300005;
    // v[p][mex] 记录质数 p 的倍数中，已经出现过的奇数 SG 值
    // 因为奇数的 SG 值非常小，开 50 绰绰有余
    static boolean[][] v = new boolean[MAXA][50]; 
    static int[] g = new int[MAXA];
    static int[] min_p = new int[MAXA];
    static int[] primes = new int[MAXA];
    static int cnt = 0;

    // 预处理所有的 SG 值
    static void init() {
        // 线性筛求最小质因子
        for (int i = 2; i < MAXA; i++) {
            if (min_p[i] == 0) {
                min_p[i] = i;
                primes[cnt++] = i;
                v[i][0] = true; // 质数的 SG 值为 0
            }
            for (int j = 0; j < cnt; j++) {
                int p = primes[j];
                if (p > min_p[i] || i * p >= MAXA) break;
                min_p[i * p] = p;
            }
        }

        g[1] = 0;
        for (int i = 2; i < MAXA; i++) {
            if (min_p[i] == i) { // 如果是质数
                g[i] = 0;
                continue;
            }
            if (i % 2 == 0) { // 如果是偶数，直接套公式
                g[i] = i / 2 - 1;
                continue;
            }
            
            // 如果是奇合数，分解质因数
            int temp = i;
            int[] ps = new int[10];
            int p_cnt = 0;
            while (temp > 1) {
                int p = min_p[temp];
                ps[p_cnt++] = p;
                while (temp % p == 0) temp /= p;
            }

            int mex = 0;
            while (true) {
                boolean found = false;
                for (int k = 0; k < p_cnt; k++) {
                    int p = ps[k];
                    // 情况1：这个 mex 已经在之前的奇数状态里出现过了
                    if (mex < 50 && v[p][mex]) {
                        found = true;
                        break;
                    }
                    // 情况2：这个 mex 可以由某个偶数 y 转移过来
                    // 偶数 y 的 SG 值为 mex，则 y = 2 * (mex + 1)
                    // 这个 y 必须是 p 的倍数，且 y < i
                    if ((2 * (mex + 1)) < i && (2 * (mex + 1)) % p == 0) {
                        found = true;
                        break;
                    }
                }
                if (!found) break; // 找到了最小的缺失值
                mex++;
            }
            g[i] = mex;
            // 把这个 SG 值记录到它所有的质因子中
            for (int k = 0; k < p_cnt; k++) {
                if (mex < 50) {
                    v[ps[k]][mex] = true;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        init(); // 必须一开始就预处理，不要放在循环里
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();

        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            st = new StringTokenizer(br.readLine());
            
            int xorSum = 0;
            for (int i = 0; i < n; i++) {
                int a = Integer.parseInt(st.nextToken());
                xorSum ^= g[a]; // Nim 游戏核心：全部异或起来
            }

            if (xorSum != 0) {
                sb.append("Yes\n"); // 先手必胜
            } else {
                sb.append("No\n");  // 先手必败
            }
        }
        System.out.print(sb);
    }
}
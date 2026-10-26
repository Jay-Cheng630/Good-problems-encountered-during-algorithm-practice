import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OddEvenSwap {
    static final int MOD = 998244353;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine().trim());

        int oddCount = 0;
        int readCount = 0;

        // 读取所有数字，可能跨多行
        while (readCount < N) {
            String line = br.readLine();
            if (line == null) break;
            String[] parts = line.trim().split("\\s+");
            for (String s : parts) {
                if (s.isEmpty()) continue;
                int x = Integer.parseInt(s);
                if ((x & 1) == 1) {
                    oddCount++;
                }
                readCount++;
                if (readCount >= N) break;
            }
        }

        // 预处理阶乘与逆阶乘
        long[] fact = new long[N + 1];
        long[] invFact = new long[N + 1];
        fact[0] = 1;
        for (int i = 1; i <= N; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[N] = modPow(fact[N], MOD - 2);
        for (int i = N; i >= 1; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }

        // 答案为 C(N, oddCount)
        long ans = fact[N] * invFact[oddCount] % MOD * invFact[N - oddCount] % MOD;
        System.out.println(ans);
    }

    static long modPow(long a, long e) {
        long res = 1;
        a %= MOD;
        while (e > 0) {
            if ((e & 1) == 1) {
                res = res * a % MOD;
            }
            a = a * a % MOD;
            e >>= 1;
        }
        return res;
    }
}
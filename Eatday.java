public class Eatday {
    public static void main(String[] args) {
        int count = 0; // 记录和平干饭日有多少天
        long remainder = 0; // 记录当前的数字除以26的余数
        
        // 循环从第1天到第2026天
        for (int i = 1; i <= 2026; i++) {
            // 要把数字 i 拼接到原来的后面
            String strI = String.valueOf(i); // 把当天的数字变成字符串，比如 "12"
            int len = strI.length(); // 看这个数字有几位
            
            // 重点：怎么把余数往后拼接？
            // 比如原来是 "1" (余数1)，现在拼上 "2"。
            // 新的数字是 12，其实就是 1 * 10 + 2 = 12。
            // 同理，原数乘以 10 的 len 次方，再加上当天的天数 i
            long multiplier = 1;
            for (int j = 0; j < len; j++) {
                multiplier = multiplier * 10;
            }
            
            // 计算拼接后的新余数
            remainder = (remainder * multiplier + i) % 26;
            
            // 如果余数是 0，说明能被 26 整除
            if (remainder == 0) {
                count++;
            }
        }
        
        // 打印最终的答案
        System.out.println(count);
    }
}
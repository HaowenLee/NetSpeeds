import android.net.TrafficStats
import me.haowen.netspeeds.App
import me.haowen.netspeeds.MemoryConstants


object NetworkUtil {

    private var lastTotalRxBytes: Long = 0
    private var lastTimeStamp: Long = 0

    private val totalRxBytes: Long
        get() =
            if (TrafficStats.getUidRxBytes(App.context?.applicationInfo?.uid ?: 0) == TrafficStats.UNSUPPORTED.toLong())
                0 else TrafficStats.getTotalRxBytes()

    init {
        lastTotalRxBytes = totalRxBytes
        lastTimeStamp = System.currentTimeMillis()
    }

    fun getNetSpeed(): String {
        val nowTotalRxBytes = totalRxBytes
        val nowTimeStamp = System.currentTimeMillis()
        val dlTime = nowTimeStamp - lastTimeStamp
        val speed = if (dlTime <= 0) {
            0
        } else {
            (nowTotalRxBytes - lastTotalRxBytes) * 1000 / dlTime//毫秒转换
        }

        lastTimeStamp = nowTimeStamp
        lastTotalRxBytes = nowTotalRxBytes

        return byte2FitMemorySize(speed)
    }

    /**
     * 字节数转合适内存大小
     * 保留3位小数
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    private fun byte2FitMemorySize(byteNum: Long): String {
        return when {
            byteNum < 0 -> "shouldn't be less than zero!"
            byteNum < MemoryConstants.KB -> String.format("%.2fB", byteNum.toDouble())
            byteNum < MemoryConstants.MB -> String.format("%.2fKB", byteNum.toDouble() / MemoryConstants.KB)
            byteNum < MemoryConstants.GB -> String.format("%.2fMB", byteNum.toDouble() / MemoryConstants.MB)
            else -> String.format("%.2fGB", byteNum.toDouble() / MemoryConstants.GB)
        }
    }
}
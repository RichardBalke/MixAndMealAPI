package api.service


import net.schmizz.sshj.xfer.LocalFileFilter
import net.schmizz.sshj.xfer.LocalSourceFile
import java.io.ByteArrayInputStream

class ByteArraySourceFile(
    private val name: String,
    private val bytes: ByteArray
) : LocalSourceFile {

    override fun getName(): String = name

    override fun getLength(): Long = bytes.size.toLong()

    override fun isFile(): Boolean = true

    override fun isDirectory(): Boolean = false

    override fun getInputStream() = ByteArrayInputStream(bytes)

    override fun getChildren(filter: LocalFileFilter?): Iterable<LocalSourceFile?> =
        emptyList()

    override fun providesAtimeMtime(): Boolean =
        false

    override fun getLastAccessTime(): Long =
        System.currentTimeMillis()

    override fun getLastModifiedTime(): Long =
        System.currentTimeMillis()

    override fun getPermissions(): Int = 420

}

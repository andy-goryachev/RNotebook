// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.notebook.util.InlineHelp;


/** "FS" object in the global context */
public class FS
{
	public FS()
	{
	}
	
	
	/*
	fs.rename(oldPath, newPath, callback)
	fs.renameSync(oldPath, newPath)
	fs.ftruncate(fd, len, callback)
	fs.ftruncateSync(fd, len)
	fs.truncate(path, len, callback)
	fs.truncateSync(path, len)
	fs.chown(path, uid, gid, callback)
	fs.chownSync(path, uid, gid)
	fs.fchown(fd, uid, gid, callback)
	fs.fchownSync(fd, uid, gid)
	fs.lchown(path, uid, gid, callback)
	fs.lchownSync(path, uid, gid)
	fs.chmod(path, mode, callback)
	fs.chmodSync(path, mode)
	fs.fchmod(fd, mode, callback)
	fs.fchmodSync(fd, mode)
	fs.lchmod(path, mode, callback)
	fs.lchmodSync(path, mode)
	fs.stat(path, callback)
	fs.lstat(path, callback)
	fs.fstat(fd, callback)
	fs.statSync(path)
	fs.lstatSync(path)
	fs.fstatSync(fd)
	fs.link(srcpath, dstpath, callback)
	fs.linkSync(srcpath, dstpath)
	fs.symlink(srcpath, dstpath, [type], callback)
	fs.symlinkSync(srcpath, dstpath, [type])
	fs.readlink(path, callback)
	fs.readlinkSync(path)
	fs.realpath(path, [cache], callback)
	fs.realpathSync(path, [cache])
	fs.unlink(path, callback)
	fs.unlinkSync(path)
	fs.rmdir(path, callback)
	fs.rmdirSync(path)
	fs.mkdir(path, [mode], callback)
	fs.mkdirSync(path, [mode])
	fs.readdir(path, callback)
	fs.readdirSync(path)
	fs.close(fd, callback)
	fs.closeSync(fd)
	fs.open(path, flags, [mode], callback)
	fs.openSync(path, flags, [mode])
	fs.utimes(path, atime, mtime, callback)
	fs.utimesSync(path, atime, mtime)
	fs.futimes(fd, atime, mtime, callback)
	fs.futimesSync(fd, atime, mtime)
	fs.fsync(fd, callback)
	fs.fsyncSync(fd)
	fs.write(fd, buffer, offset, length, position, callback)
	fs.writeSync(fd, buffer, offset, length, position)
	fs.read(fd, buffer, offset, length, position, callback)
	fs.readSync(fd, buffer, offset, length, position)
	fs.readFile(filename, [options], callback)
	fs.readFileSync(filename, [options])
	fs.writeFile(filename, data, [options], callback)
	fs.writeFileSync(filename, data, [options])
	fs.appendFile(filename, data, [options], callback)
	fs.appendFileSync(filename, data, [options])
	fs.watchFile(filename, [options], listener)
	fs.unwatchFile(filename, [listener])
	fs.watch(filename, [options], [listener])
	fs.exists(path, callback)
	fs.existsSync(path)
	*/
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("FS provides filesystem operations:");
		//h.a("FS.loadImage(filename)", "loads image");
		return h.toString();
	}
}

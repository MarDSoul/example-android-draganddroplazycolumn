package example.mardsoul.draganddroplazycolumn.util

fun <T> MutableList<T>.move(from: Int, to: Int) {
	if (from == to) return
	val element = this.removeAt(from)
	this.add(to, element)
}
/**
 * Returns a list of lists, each built from elements of all lists with the same indexes.
 * Result is same as transposing a matrix.
 * Output has length of longest input list.
 *
 * example:
 *
 *     [
 *       [a1, a2],
 *       [b1, b2, b3]
 *     ]
 *
 *  will be transposed into:
 *
 *     [
 *       [a1, b1],
 *       [a2, b2],
 *       [b3],
 *     ]
 *
 */
fun <T> List<MutableList<T>>.transpose(): MutableList<MutableList<T>> {
    val maxSize = this.maxOfOrNull(List<T>::size) ?: 0
    val list = ArrayList<MutableList<T>>(maxSize)

    val iterators = this.map { it.iterator() }
    var i = 0
    while (i < maxSize) {
        list.add(
            iterators.mapNotNull {
                if (it.hasNext()) {
                    it.next()
                } else null
            }.toMutableList()
        )
        i++
    }

    return list
}

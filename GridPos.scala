import grid.CompassDir


/** An object of type `GridPos` represents a pair of integer coordinates on two axes named x and y.
  * Such a pair can be used to reference a point on a [[Grid]].
  *
  * In this coordinate system, `x` increases "eastwards" and `y` increases "southwards".
  *
  * `GridPos` objecta are immutable.
  *
  * This class has an alias in the top-level package [[o1]], so it’s accessible to students
  * simply via `import o1._`.
  *
  * @param x  an x coordinate
  * @param y  a y coordinate */
case class GridPos(val x: Int, val y: Int) {

  def relative(direction: CompassDir, distance: Int) =
    new GridPos(this.x + direction.xStep * distance,
                this.y + direction.yStep * distance)


  def neighbor(direction: CompassDir): GridPos = this.relative(direction, 1)

  def pathTowards(direction: CompassDir): LazyList[GridPos] = LazyList.from(1).map( this.relative(direction, _) )

  def ==(another: GridPos): Boolean = this.x == another.x && this.y == another.y

  def xDiff(another: GridPos): Int = another.x - this.x

  def yDiff(another: GridPos): Int = another.y - this.y

  def diff(another: GridPos): (Int, Int) = (this.xDiff(another), this.yDiff(another))

  def xDirectionOf(another: GridPos): Option[CompassDir] = {
    val dx = this.xDiff(another)
    if (dx < 0) Some(CompassDir.West) else if (dx > 0) Some(CompassDir.East) else None
  }

  def yDirectionOf(another: GridPos): Option[CompassDir] = {
    val dy = this.yDiff(another)
    if (dy < 0) Some(CompassDir.North) else if (dy > 0) Some(CompassDir.South) else None
  }

  def distance(another: GridPos): Int = this.xDiff(another).abs + this.yDiff(another).abs

  override def toString = s"($x,$y)"

}

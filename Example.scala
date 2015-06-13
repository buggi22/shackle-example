package example

import scala.collection.immutable.ListMap
import shackle.ConstraintSolver
import shackle.Constraints._

object Example {
  def main(args: Array[String]): Unit = {
    val domains = ListMap(
      "a" -> List(3),
      "b" -> List(1, 2, 3),
      "c" -> List(2),
      "d" -> List(1, 2, 3, 4))
    val constraints = Seq(allDifferent(vars = List("a", "b", "c", "d")))
    val solver = new ConstraintSolver(domains, constraints)
    val solution = solver.solve()
    println(solution)
  }
}

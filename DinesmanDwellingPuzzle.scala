package example

import scala.collection.immutable.ListMap

import shackle.ConstraintSolver
import shackle.Constraints._

object DinesmanDwellingPuzzle {

  val floors = (1 to 5).toList

  val people = List("Baker", "Cooper", "Fletcher", "Miller", "Smith")

  val domains = ListMap(people.map { p => (p, floors) } : _*)
 
  def notAdjacent(var1: String, var2: String) =
      binaryNumericPredicate(var1, var2, (x1, x2) => math.abs(x1 - x2) != 1)
 
  val constraints = List(
      // Baker, Cooper, Fletcher, Miller, and Smith live on different floors
      // of an apartment house that contains only five floors.
      allDifferent(vars = people),
      // Baker does not live on the top floor.
      valueIsNot("Baker", 5),
      // Cooper does not live on the bottom floor.
      valueIsNot("Cooper", 1),
      // Fletcher does not live on either the top or the bottom floor.
      valueIsNot("Fletcher", 1),
      valueIsNot("Fletcher", 5),
      // Miller lives on a higher floor than does Cooper.
      binaryNumericPredicate("Miller", "Cooper", _ > _),
      // Smith does not live on a floor adjacent to Fletcher's.
      notAdjacent("Smith", "Fletcher"),
      // Fletcher does not live on a floor adjacent to Cooper's.
      notAdjacent("Fletcher", "Cooper"))
  
  val solver = ConstraintSolver(domains, constraints)

  def main(args: Array[String]): Unit = {
    var foundSolution = false
    for (solution <- solver.allSolutions) {
      foundSolution = true
      println("Solution:")
      for ((person, floor) <- solution.toList.sortBy(_._2.asInstanceOf[Integer])) {
        println(s"${person} lives on floor ${floor}")
      }
    }
    if (!foundSolution) {
      println("No solution found.")
    }
  }
}

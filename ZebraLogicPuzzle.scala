package example

import scala.collection.immutable.ListMap

import shackle.{ConstraintSolver, Constraints}

object ZebraLogicPuzzle {

  val attributes = ListMap(
      "color" -> List("red", "green", "ivory", "yellow", "blue"),
      "nation" -> List("England", "Spain", "Norway", "Ukraine", "Japan"),
      "candy" -> List("Hershey bars", "Kit Kats", "Smarties", "Snickers", "Milky Ways"),
      "drink" -> List("OJ", "tea", "coffee", "milk", "water"),
      "pet" -> List("dog", "fox", "snails", "horse", "zebra"))
      // Note: "zebra" and "water" don't appear in the clues, but only in the problem statement
  
  val houses = (1 to 5).toList

  val domains = for (
      (attributeName, values) <- attributes;
      value <- values) yield {
    (value, houses)
  }
  
  val allDiffConstraints = attributes.map { case (attributeName, values) =>
    Constraints.allDifferent(values)
  }
  
  val clueConstraints = List(
      // The Englishman lives in the red house.
      Constraints.sameValue("England", "red"),
      // The Spaniard owns the dog.
      Constraints.sameValue("Spain", "dog"),
      // The Norwegian lives in the first house on the left.
      Constraints.valueIs("Norway", 1),
      // The green house is immediately to the right of the ivory house.
      Constraints.difference("green", "ivory", 1),
      // The man who eats Hershey bars lives in the house next to the man with the fox.
      Constraints.absoluteDifference("Hershey bars", "fox", 1),
      // Kit Kats are eaten in the yellow house.
      Constraints.sameValue("Kit Kats", "yellow"),
      // The Norwegian lives next to the blue house.
      Constraints.absoluteDifference("Norway", "blue", 1),
      // The Smarties eater owns snails.
      Constraints.sameValue("Smarties", "snails"),
      // The Snickers eater drinks orange juice.
      Constraints.sameValue("Snickers", "OJ"),
      // The Ukrainian drinks tea.
      Constraints.sameValue("Ukraine", "tea"),
      // The Japanese eats Milky Ways.
      Constraints.sameValue("Japan", "Milky Ways"),
      // Kit Kats are eaten in a house next to the house where the horse is kept.
      Constraints.absoluteDifference("Kit Kats", "horse", 1),
      // Coffee is drunk in the green house.
      Constraints.sameValue("coffee", "green"),
      // Milk is drunk in the middle house.
      Constraints.valueIs("milk", 3))
  
  val constraints = allDiffConstraints ++ clueConstraints
  
  val solver = new ConstraintSolver(domains, constraints.toSeq)

  def main(args: Array[String]): Unit = {
    val solution = solver.solve()
    solution match {
      case None => println("No solution found")
      case Some(sol) => {
        for (house <- houses) {
          print(s"house ${house}:")
          for ((attributeName, values) <- attributes;
              value <- values) {
            if (sol(value) == house) {
              print(s"\t${value}")
            }
          }
          println()
        }
      }
    }
  }
}

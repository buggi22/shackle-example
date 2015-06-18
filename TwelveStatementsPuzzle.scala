package example

import scala.collection.immutable.ListMap

import shackle.sat.SatSolver
import shackle.sat.Expressions._

object TwelveStatementsPuzzle {

  val statementNames = ListMap(
      "s1" -> "1. This is a numbered list of twelve statements.",
      "s2" -> "2. Exactly 3 of the last 6 statements are true.",
      "s3" -> "3. Exactly 2 of the even-numbered statements are true.",
      "s4" -> "4. If statement 5 is true, then statements 6 and 7 are both true.",
      "s5" -> "5. The 3 preceding statements are all false.",
      "s6" -> "6. Exactly 4 of the odd-numbered statements are true.",
      "s7" -> "7. Either statement 2 or 3 is true, but not both.",
      "s8" -> "8. If statement 7 is true, then 5 and 6 are both true.",
      "s9" -> "9. Exactly 3 of the first 6 statements are true.",
      "s10" -> "10. The next two statements are both true.",
      "s11" -> "11. Exactly 1 of statements 7, 8 and 9 are true.",
      "s12" -> "12. Exactly 4 of the preceding statements are true.")

  val vars = statementNames.map(_._1).toSeq
 
  val expression = and(
      // 1. This is a numbered list of twelve statements."
      lit("s1"),
      // 2. Exactly 3 of the last 6 statements are true."
      equiv(lit("s2"), numberTrue(3, (7 to 12).map { i => lit("s" + i )})),
      // 3. Exactly 2 of the even-numbered statements are true."
      equiv(lit("s3"), numberTrue(2, (1 to 6).map { i => lit("s" + (i*2)) })),
      // 4. If statement 5 is true, then statements 6 and 7 are both true."
      equiv(lit("s4"), implies(lit("s5"), and(lit("s6"), lit("s7")))),
      // 5. The 3 preceding statements are all false."
      equiv(lit("s5"), not(or(lit("s2"), lit("s3"), lit("s4")))),
      // 6. Exactly 4 of the odd-numbered statements are true."
      equiv(lit("s6"), numberTrue(4, (1 to 6).map { i => lit(s"s${i*2-1}") })),
      // 7. Either statement 2 or 3 is true, but not both."
      equiv(lit("s7"), xor(lit("s2"), lit("s3"))),
      // 8. If statement 7 is true, then 5 and 6 are both true."
      equiv(lit("s8"), implies(lit("s7"), and(lit("s5"), lit("s6")))),
      // 9. Exactly 3 of the first 6 statements are true."
      equiv(lit("s9"), numberTrue(3, (1 to 6).map { i => lit("s" + i) })),
      // 10. The next two statements are both true."
      equiv(lit("s10"), and(lit("s11"), lit("s12"))),
      // 11. Exactly 1 of statements 7, 8 and 9 are true."
      equiv(lit("s11"), numberTrue(1, List("s7", "s8", "s9").map(lit _))),
      // 12. Exactly 4 of the preceding statements are true."
      equiv(lit("s12"), numberTrue(4, (1 to 11).map { i => lit("s" + i) })))

  def main(args: Array[String]): Unit = {
    val solver = SatSolver(vars, expression)

    var foundSolution = false
    for (solution <- solver.allSolutions) {
      foundSolution = true
      println("Solution:")
      for ((name, value) <- solution) {
        println(name + ": " + value + " - " + statementNames(name))
      }
    }
    if (!foundSolution) {
      println("No solution found.")
    }
  }
}

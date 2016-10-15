package de.htwg.se.setGame

import de.htwg.se.setGame.model.Student

object Hello {
  def main(args: Array[String]): Unit = {
    val student = Student("Your Name")
    println("Hello, " + student.name)
  }
}

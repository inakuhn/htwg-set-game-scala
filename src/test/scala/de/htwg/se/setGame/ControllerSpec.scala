package de.htwg.se.setGame

import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class ControllerSpec extends WordSpec {

  "Controller" should {
    "have instance" in {
      Controller(null) shouldBe a [Controller]
    }
  }
}

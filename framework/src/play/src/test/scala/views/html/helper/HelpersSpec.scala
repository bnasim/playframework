package views.html.helper

import org.specs2.mutable.Specification
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.Lang

object HelpersSpec extends Specification {
  import FieldConstructor.defaultField
  import Lang.defaultLang

  "@inputText" should {

    "allow setting a custom id" in {

      val body = inputText.apply(Form(single("foo" -> Forms.text))("foo"), 'id -> "someid").body

      val idAttr = "id=\"someid\""
      body must contain(idAttr)

      // Make sure it doesn't have it twice, issue #478
      body.substring(body.indexOf(idAttr) + idAttr.length) must not contain(idAttr)
    }

    "default to a type of text" in {
      inputText.apply(Form(single("foo" -> Forms.text))("foo")).body must contain("type=\"text\"")
    }

    "allow setting a custom type" in {
      val body = inputText.apply(Form(single("foo" -> Forms.text))("foo"), 'type -> "email").body

      val typeAttr = "type=\"email\""
      body must contain(typeAttr)

      // Make sure it doesn't contain it twice
      body.substring(body.indexOf(typeAttr) + typeAttr.length) must not contain(typeAttr)
    }
  }


  "@checkboxGroup" should {
     "allow to check more than one checkbox" in {
       val form = Form(single("hobbies" -> Forms.list(Forms.text))).fill(List("S", "B"))
       val body = inputCheckboxGroup.apply(form("hobbies"), Seq(("S", "Surfing"), ("B", "Biking"))).body

       // Append [] to the name for the form binding
       body must contain( "name=\"hobbies[]\"" )

       body must contain( """<input type="checkbox" id="hobbies_S" name="hobbies[]" value="S" checked >""" )
       body must contain( """<input type="checkbox" id="hobbies_B" name="hobbies[]" value="B" checked >""" )
     }
  }

  "@select" should {

    "allow setting a custom id" in {

      val body = select.apply(Form(single("foo" -> Forms.text))("foo"),Seq(("0", "test")), 'id -> "someid").body

      val idAttr = "id=\"someid\""
      body must contain(idAttr)

      // Make sure it doesn't have it twice, issue #478
      body.substring(body.indexOf(idAttr) + idAttr.length) must not contain(idAttr)
    }

    "Work as a simple select" in {
      val form = Form(single("foo" -> Forms.text)).fill("0")
      val body = select.apply(form("foo"), Seq(("0", "test"), ("1", "test"))).body

      body must contain( "name=\"foo\"" )

      body must contain( """<option value="0" selected>""" )
      body must contain( """<option value="1" >""" )
    }

    "Work as a multiple select" in {
      val form = Form(single("foo" -> Forms.list(Forms.text))).fill(List("0", "1"))
      val body = select.apply(form("foo"), Seq(("0", "test"), ("1", "test")), 'multiple -> None).body

      // Append [] to the name for the form binding
      body must contain( "name=\"foo[]\"" )
      body must contain( "multiple" )

      body must contain( """<option value="0" selected>""" )
      body must contain( """<option value="1" selected>""" )
    }

  }
}

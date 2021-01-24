package example

import com.thoughtworks.binding.Binding.{BindingSeq, Var, Vars}
import com.thoughtworks.binding.Binding
import org.lrng.binding.html
import org.scalajs.dom.{Event, Node, document}
import org.scalajs.dom.ext.Ajax

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.JSON


object ScalaJSExample {

  implicit def makeIntellijHappy(x: scala.xml.Elem): Binding[Node] = ???

  implicit def makeIntellijHappy(x: scala.xml.NodeBuffer): BindingSeq[Node] = ???

  /**
   * Ajax Request to server, updates data state with number
   * of requests to count.
   *
   * @param data
   */
  def countRequest(data: Var[String]) = {
    val url = "http://localhost:9000/count"
    Ajax.get(url).foreach(xhr =>
      data.value = JSON.parse(xhr.responseText).count.toString)
  }


  @html
  def render: BindingSeq[Node] = {
    val data = Var("")
    countRequest(data) // initial population

    case class Contact(name: Var[String], email: Var[String])

    val datas = Vars.empty[Contact]


    <div>
      <button onclick={event: Event => countRequest(data)}>
        Boop
      </button>
      From Play: The server has been booped
      {data.bind}
      times. Shared Message:
      {shared.SharedMessages.itWorks}
      .
    </div>


      <div>
        <button onclick={event: Event => datas.value += Contact(Var("Yang Bo"), Var("yang.bo@rea-group.com"))}>
          Add a contact
        </button>
      </div>
      <div>
        <table style="border: 1px solid black">
          <tr>
            <th>name</th>
            <th>email</th>
          </tr>{for (contact <- datas) yield {
          <tr>
            <td>
              {contact.name.bind}
            </td>
            <td>
              {contact.email.bind}
            </td>
          </tr>
        }}
        </table>
      </div>
  }

  def main(args: Array[String]): Unit = {
    html.render(document.body, render)
  }
}

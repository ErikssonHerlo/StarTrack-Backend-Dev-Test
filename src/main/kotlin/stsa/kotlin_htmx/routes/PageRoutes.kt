package stsa.kotlin_htmx.routes

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*
import stsa.kotlin_htmx.pages.*
import stsa.kotlin_htmx.pages.LinkMainPage
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("stsa.kotlin_htmx.Routes")

fun Application.configurePageRoutes() {
    routing {
        get {
            call.respondHtmlTemplate(MainTemplate(template = EmptyTemplate(), "Front page")) {
                mainSectionTemplate {
                    emptyContentWrapper {
                        section {
                            p {
                                +"Startrack Backend Dev Test by Eriksson Hernández"
                            }
                        }
                    }
                }
            }
        }

        route("/link") {
            val linkMainPage = LinkMainPage()
            get {
                linkMainPage.renderMainPage(this)
            }
        }

        route("/skins") {
            val skinsPage = SkinsPage()
            get { skinsPage.render(this) }
        }

        route("/agents") {
            val agentsPage = AgentsPage()
            get { agentsPage.render(this) }
        }

        route("/crates") {
            val cratesPage = CratesPage()
            get { cratesPage.render(this) }
        }

        route("/keys") {
            val keysPage = KeysPage()
            get { keysPage.render(this) }
        }

    }
}

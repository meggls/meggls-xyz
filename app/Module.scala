import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import xyz.meggls.megglsxyz.db.InitDb

class Module extends AbstractModule with AkkaGuiceSupport {

    override def configure(): Unit = {
        bind(classOf[InitDb]).asEagerSingleton()
    }

}

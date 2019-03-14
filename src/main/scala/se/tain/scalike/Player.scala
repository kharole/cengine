package se.tain.scalike

import org.joda.time.DateTime
import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent._

case class Player(
                   id: Long,
                   name: String,
                   companyId: Option[Long] = None,
                   createdAt: DateTime,
                   deletedAt: Option[DateTime] = None) extends ShortenedNames {

  def save()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Player] = Player.save(this)(session, cxt)

  def destroy()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = Player.destroy(id)(session, cxt)
}

object Player extends SQLSyntaxSupport[Player] with ShortenedNames {

  override val columnNames = Seq("id", "name", "company_id", "created_timestamp", "deleted_timestamp")
  override val nameConverters = Map("At$" -> "_timestamp")

  // simple extractor
  def apply(p: SyntaxProvider[Player])(rs: WrappedResultSet): Player = apply(p.resultName)(rs)

  def apply(p: ResultName[Player])(rs: WrappedResultSet): Player = new Player(
    id = rs.long(p.id),
    name = rs.string(p.name),
    companyId = rs.longOpt(p.companyId),
    createdAt = rs.jodaDateTime(p.createdAt),
    deletedAt = rs.jodaDateTimeOpt(p.deletedAt)
  )

  // SyntaxProvider objects
  lazy val p = Player.syntax("p")

  // reusable part of SQL
  private val isNotDeleted = sqls.isNull(p.deletedAt)

  // find by primary key
  def find(id: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[Player]] = {
    withSQL {
      select
        .from(Player as p)
        .where.eq(p.id, id).and.append(isNotDeleted)
    }.map(Player(p)).single().future()
  }

  // Player with company(optional) with skills(many)
  def findAll()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[Player]] = {
    withSQL {
      select
        .from[Player](Player as p)
        .where.append(isNotDeleted)
        .orderBy(p.id)
    }.map(Player(p)).list.future
  }

  def countAll()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Long] = withSQL {
    select(sqls.count).from(Player as p).where.append(isNotDeleted)
  }.map(rs => rs.long(1)).single.future.map(_.get)

  def countBy(where: SQLSyntax)(
    implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Long] = withSQL {
    select(sqls.count).from(Player as p).where.append(isNotDeleted).and.append(sqls"${where}")
  }.map(_.long(1)).single.future.map(_.get)

  def create(name: String, companyId: Option[Long] = None, createdAt: DateTime = DateTime.now)(
    implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Player] = {
    for {
      id <- withSQL {
        insert.into(Player).namedValues(
          column.name -> name,
          column.companyId -> companyId,
          column.createdAt -> createdAt)
          .returningId // if you run this example for MySQL, please remove this line
      }.updateAndReturnGeneratedKey.future
    } yield Player(
      id = id,
      name = name,
      companyId = companyId,
      createdAt = createdAt
    )
  }

  def save(m: Player)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Player] = {
    withSQL {
      update(Player).set(
        column.name -> m.name,
        column.companyId -> m.companyId
      ).where.eq(column.id, m.id).and.isNull(column.deletedAt)
    }.update.future.map(_ => m)
  }

  def destroy(id: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
    update(Player).set(column.deletedAt -> DateTime.now).where.eq(column.id, id)
  }.update.future()

}
package se.tain

import akka.actor.{ActorSystem, TypedActor, TypedProps}
import se.tain.casino.services.internal.integration.InternalCasinoService

object Launcher extends App {
  val system = ActorSystem("casino-engine")
  val typedActor: InternalCasinoService = TypedActor(system).typedActorOf(TypedProps(classOf[InternalCasinoService], new ActorBackedIntegrationService(system)), "integration-server")
}


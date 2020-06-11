package org.spa.controller;

/**
 * Represents a system in the application
 *
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public interface Service {
   /**
    * Start this system. Implementation usually load stuff from the repository, or
    * initializing its state
    */
   void start();

   /**
    * Stop this system. Implementation usually saves stuff to the repository, or free
    * its allocated memory/workers
    */
   void stop();
}


<h1> Rod Wars README</h1>

This is the documentation for Rod Wars.

* *_Server start-up / shut-down_*
    * I use the 'IPlugin' interface as a template for the main class (RodWars). It has methods to instantiate variables, as well as register listeners and commands.
* *_Building and keeping track of Rods_*
    * I use the abstract class 'Rod' as a super class that all individual rod classes can extend, as well as a container that stores all initialized rods inside a static HashMap<RodName, Rod>.
    * Instead of manually calling each Rod class constructor, I coded a method in the 'Rod' class called 'buildAllRods()' that loops through all classes in the 'rods' package and calls the constructors of each.
      * NOTE: this method uses hard-coded file paths. Everything should be fine as long as the 'rods' package is directly in the 'me.liam.rodwars' package. If 'rods' package is renamed, simply change the parameter of the 'buildAllRods' method accordingly.
* *_Registering Commands_*
  * Similar to how I looped through classes in the 'rods' package to instantiate them, I did the same for registering commands. The 'ICommand' interface handles this.
  * Notably, ICommand has two methods: the abstract 'getCommand' method which requires all implementing classes to have, and then a default 'registerAllCommands(instance, package)' method, which takes in an instance of the main class as well as the name of the package that has all of the command classes.
    * NOTE: My code here assumes that this package is directly in the 'me.liam.rodwars' package. It ALSO uses the hard-coded absolute path of the 'me.liam.rodwars' package, so any moving of folders will cause the code to not work.
* *_Handling Rod Abilities_*
  * The way I am handling Rod special abilities/powers is through listeners, of course, The way I register these listeners is through the abstract 'Rod' class, which has the abstract method 'getListeners', requiring all Rod classes to return a list of listeners. For each initialized rod, this list is then iterated through and each listener for each rod is registered.
    * NOTE: This code is written in the 'registerAllListeners()' method in the Rod class.


* *_Ideas_*
  * Make the Shiny Rod charge up when right-click is held. (Maybe an event for releasing right-click?)
  * Add scope (spy glass effect) to Shiny Rod
  * Build a cooldown and mana system using actionbar messages
    * Make shiny rod mana-based rather than cooldown based.
  * Make shiny rod solely a sniper (no weird gravity effects)
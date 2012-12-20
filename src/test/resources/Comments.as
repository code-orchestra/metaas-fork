/**
 *
 * javadoc
 *
 * @author Anton.I.Neverov
 *
 */
// comment before package
package zxc.rty {

    // comment before class
	public class Foo {

        /**
         * constructor
         */
		public function Foo() {
			super();
		}
	
		// static comment before1
		// static comment before2
		public static var myVar:int; // static comment after
	
		// comment before javadoc
        /**
         *
         *   javadoc that describes fields
         *
         *   @param abcde
         *
         *
         */
        private var a:int;
        // comment before second field
        private var b:int, c:int, d:int; // comment after second field
        private var e:int; /* Very strange
                                        comment */

        // comment before method's javadoc
        /**
         *   javadoc before method
         *     @param pam pam!
         */
        // comment after method's javadoc
		public static function myFunc():int {
			
			/* Multi
				multi
				multiline
				comment
				*/
			
		    a = 1; // comment after expr statement

            //
            do {
                // comment before expr (a = 2)
                a = 2; // comment after expr (a = 2)
                /* remark */
		        break; // comment after break
		    }
		    while (true); // comment after DoWhile statement
			
			// Suddenly!
			
            for (a = 0; a < 5; a++) {
				continue; // comment after continue
		    }

			return a; // comment after return
		}
	}
}

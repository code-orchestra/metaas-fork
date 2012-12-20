package foo.bar {

    use namespace mx_internal_2;

	public class Foo extends Bar {

        use namespace mx_internal_3;        

        public function myFunc() : void {
            use namespace mx_internal;
        }
	}
}
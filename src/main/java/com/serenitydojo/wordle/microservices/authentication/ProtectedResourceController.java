@RestController
public class ProtectedResourceController {

    @GetMapping("/protected")
    @Secured("ROLE_USER") // Role-based authorization
    public String protectedResource() {
        return "This is a protected resource.";
    }
}
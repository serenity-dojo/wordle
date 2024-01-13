@RestController
public class AuthController {
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // Authenticate the user (e.g., using Spring Security's authentication manager)
        // If authentication is successful, generate a JWT
        String token = JwtUtil.generateToken(request.getUsername());
        return token;
    }
}
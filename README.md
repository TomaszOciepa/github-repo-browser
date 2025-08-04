# GitHub Repo Browser

This application fetches public GitHub repositories of a user, excluding forks, along with the list of branches and the latest commit SHA for each branch.

## 📦 Technologies

- Java 21
- Spring Boot 3
- REST API (RestTemplate)
- JUnit 5
- Maven

## 🚀 Running the application

1. **Clone the repository:**

   ```bash
   git clone https://github.com/your-username/github-repo-browser.git
   cd github-repo-browser

2. **Run the application locally:**

	Using your IDE (like IntelliJ), run the main class: 
	com.tom.ociepa.github_repo_browser.GithubRepoBrowserApplication

	Or from the terminal: 
	```bash
	./mvnw spring-boot:run

3. **The application runs by default on port 8080.**


 ## Endpoint to fetch user repositories:

	GET http://localhost:8080/users/{username}/repos

## GitHub API rate limits:

**Limits:**
GitHub enforces rate limits for unauthenticated users:

60 requests per hour per IP address.

This applies to all unauthenticated requests, including those made by this application.

If the limit is exceeded, the API will respond with a 403 error (rate limit exceeded).



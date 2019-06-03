package lt.viko.eif.dbchecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  private VoterRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    repository.deleteAll();

    // save a couple of voters
    repository.save(new Voter("Jolita", "Brans", "49101134312",
        "-----BEGIN PUBLIC KEY-----\\nMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAikulao+9yHX0lS2RLb9d\\no80jZ6WfojztllE/AWbW3wHDXAkJY3wDvEoRWf+rJj8hmfmE7ClTwCos7Wyoj1tT\\n2lK2+t0XTMCqZJKNmVhSe9e0CC9pUHL2+pijEkh6hPUUwChi/9sspIZ7WR32AvjC\\n3cM6CC89cX7P9SbnBol4zSaJc1H67sVRU4npJTWZgSIRmPnsLp8qIIydKjiu+XDS\\nTed7zW75X6m+G6uK5ddNqc6xkN30TAJRweuwdY7CP2MIfhByPQ5XNDfmoMueOW/Y\\nmrGlWMBXIxTEnQN1WAXpoJ60T19kS3d9qHm046omjF8PqoVC5+It5FLGlz9GNlyf\\n2t937XD5g9Oqir0/9SHzwLUw92kKnFZNkWCDFVqX+WjF+rhVMSUuq0fZW89SwbVh\\nvzuTAi73mWsyyA/2BzTuYxboXQbR/iWz3Ep8+8BVDRgi3tRFR9+TytiiSQXpNlmB\\nbFw5ZyOQ9Ed/SImCTA3kBufXgZXoRlnbp3lO+cX594XN0EKwS4bprsNbG4njwCVC\\nejk7Yu/db40yfplHMdm32iHNDwz54q1Jub06ONQmhrA3aZG4ZpzxE746UaCk84MW\\nBdOpd9Q3e13dF9WKzj65BqkqJoIU/91rS8BqkbYJ2L7rlpAWK98bZabjCmcs6580\\nJEs/ZHn+roHJmcgcwE5MRU0CAwEAAQ==\\n-----END PUBLIC KEY-----",
        true));
    repository.save(new Voter("Jonas", "Klevas", "36702127422",
        "-----BEGIN PUBLIC KEY-----\\nMIICITANBgkqhkiG9w0BAQEFAAOCAg4AMIICCQKCAgB2gvJ17ptL+Ks7M1TRbnQL\\nD68Uoc6jJNZqQiDbe8PXm/toz4rvp8hIATqvEAWAA6vDRtCS5rT2LVMy0LeLWmVB\\nYP8z/S95phQ53IpC8GP7Ve+inpDfEm0lhyVATDv9H5AlK/uItVVsuPkoM/cAwcr/\\nbWb3PhhSW48p/podJ1zuoXp5AZ2oewN3PxH960V5T83JTWml+nmNGBDG75Cl7c20\\nQ8gsaCPJS9T28ZDUzkiJzqvGLSsu51mmmi4/DYB1F8VP8XT8p04OoN1QgyZb0PwZ\\nKvfU0mnBPu47BrbGJRK8/qQ1D48yrzwnJrU5W9Jn+9DD3wgQgkGGQd8Y4K1huQ4Q\\nzmzGX0xx39CvVB/3MYFArXsl6+0RVJiwwyGIKNKW+bXBu+BlVHWaxJ5bO8QwZonj\\nTSMsS05nMqCghFcoUrnXXZRjEagsCoEerpos/861z98h03KEykDwWMmG1+k1WcA+\\nxvfoPgu9Kjo6dEWiGzYx4vW8XPR+hQvX2pxSjbTL/y5kVpxxh6EXiWKISaVwzsyK\\nY1JS2WPiKXJnM+vNUmt+EHXnLzYeXjxCQwQGnuQEv1ju2+NM0RScCAazQxpdsaM6\\n6y/Cg5PdLB8yTV7gSMXUUgeuUs1feyqceFGIRQehx9cwQZU5tfg8rKO74G6wenq1\\ngog/bdZGI/ZPUCSWP4Hd0QIDAQAB\\n-----END PUBLIC KEY-----",
        true));
    repository.save(new Voter("Juozas", "Parnauskas", "38905258472",
        "-----BEGIN PUBLIC KEY-----\\nMIICITANBgkqhkiG9w0BAQEFAAOCAg4AMIICCQKCAgBeBx+1rZGxIs7mkp+bWlG3\\n8G68Jy4rbHFQtV8mnM+J2/f3aJYaCprPAZv3HCeOaRFjyslEQAaOU+IZkWd9kJN1\\njeMSdOwWoJCvuCfhL+0VWTmQ0xhvvcMMnUeMGnLpYotzVV2HgxpDNwKuS6F3IjDC\\nF1uZq+mNhFGVET6eOMcVRi4cD54k4AR0Y2fgrlp7NVEgZANxyyQB+p8F85AS38/n\\norWDeLif45gptL7rIPz7cLHTYrVTO11Ajv0spmqiSSGbsW7irsyAeLhCmzRq0cL2\\nJoNEn3BPP19mUb/nnnD3MiYz7JGMe5Q3Aig+Xh34JqHxctvlW94iogAXCg2IBydm\\n2vDsT7qn7qenBkLIjk8Fc+h0SHVK0id4tHpGRJPO9lo355UBwS7zj+QK6XBNqTrl\\ntcOmDS37dgdP/857bPyvf6S4OY6cVe7PKSeiopqMuIxxjPRbg6Q8T+KXpBhWLAvk\\nlHbAk+jhXemPX3y/dlTsWGPbIr0zeHuLCUbMUlwgXw4btI10V62CGxfjgbaICi3f\\n2c5RY2Lv4Pd1FiZb6+tZYNQZkchZN39D+aAxOXOviUPzpANcraSrppcF1NBgRxez\\nLoQR7terEbzjrYjVMj1tsKNoHdoT+DBS5NIhXtZ8vREL5siyfKtV2NpMZcoNPyK0\\nJBOd3PDxESQnO4eopePkWwIDAQAB\\n-----END PUBLIC KEY-----",
        true));
    repository.save(new Voter("Kazimieras", "Butkus", "37112027462",
        "-----BEGIN PUBLIC KEY-----\\nMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAoMdjJ1ytHlwMEohnm4lg\\niTQEAudEDGHUOYzPACbAPUOaBJ91VpB2ausTctSdkZ3pP5j/l5CznXSokdUyShOe\\nwBibT5RSgMjaNKINwbctBetx7vbDM/4RRgxlQpMIJpiK+mDM0FEQyS52+0wuYjLB\\nnBvJH8slQ9BwEovvSFmFFiTuDaxiJFofODkWjTIvLJbqYz31M3fEutB0c0rY1I2U\\nVkzwe/C3zuSg/khOx3P9AB9XpnHYLGTsEQNrYeCmj1x/xKP1FPDfXs5tEb5vXgvm\\nUO0n0Y8RGQbGZibBFyzhA6qz8M2BeXkY3fSkCbw+aOSlO8XjN5rV6QM4zUnFFbT8\\nDLMW4CqAYBnYlCwvQeHe6OGk7KQZQUSPumnEL/8CUu4+ctvA6vFiOl9NtdBO6kwh\\ne0NsLttArEC9F1yjGTU/22sZFSvNMy680Dzh7ZkI1vKTim5uyxRihGruZqhly61/\\n8D6M0jb8V2LL9G2aDeGpnW8Oz+KcVQi2qndAIgT+QXTjxnC8aXYVL/igWhFp/+/T\\nPxIQsgvw2HkOBXxdj0E2Q3vnrYiKwgkkui/lwaV2CPAK+7QSBU9Sc+B/uDA4QMra\\nnoz7gs+wh6+l30kmob+qTGSP+V6QQXSu5Ero2VIf7lwhVVew9g5lh3sPF6fPrbsM\\nQL1OSIvsfS2qDW85WgnSRNUCAwEAAQ==\\n-----END PUBLIC KEY-----",
        true));
    repository.save(new Voter("Ona", "Genys", "49406138372",
        "-----BEGIN PUBLIC KEY-----\\nMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA16YtGb2l+8FwqE+aNFiz\\nZqxD8SRNyOaEWJISxeiex3Jtovpd9sN+IRNKZDKX9dhDztc21D6qt7UxtQR8MXgU\\nISWQhEvBPvPEA5fmL6uNblS2JuTgOs+caKM0Mi4/dApLcd2CYl22ZtdjTeKiJCv+\\nWVrXLW1qQ1Rjm/eBoNlucjZaB87ACbzgmr8Y48WxaiYYUI/lm9dvBvKyZPyl4KE5\\nOXHtWmJnZ9DaqyQn2dmIQjDNZDxZ15Ci/SMWs8ZpMqGjSCLj2RbTHSrjD928n6aI\\n2RCP/uVe08aJ3316bS4niRucfe0HRTp0xcm8qQlqbf1+Ldwfb3bPuEm/MU49f8RT\\n7VvBV3fbOGzAadlkk/xLuc/MuaxoG1J3G8pYnc9g9oMSwa17ZH7BVBfNy4Fktdo9\\nms8kwGvK1LEkNUlzVMnytQClh1IMCeLyQVh8ofN5L8S17IH5j8DCCpUimCZnU9Y6\\n9jTce6GhBNyqSZBzUk4q0Puk407+ye4v/cLnouWxHMABpZ5UApgZ/jmI94yT+uWj\\ndqwxZ8hkfh50rN0ek1iZp8Zii3pOckPdfSV4I5bywSTzwOwOHn2txAe64FN6p9Do\\nBCY0lUqOJeHIfwHzgiWneprUfuF5fmKgwD4LgDQ1DpnxudJGAYiyj1YTFO8hGz2W\\n2pMBW/UXEsV4SKoxWsTsG2kCAwEAAQ==\\n-----END PUBLIC KEY-----",
        true));
    repository.save(new Voter("Tadas", "Adomaitis", "39209128472",
        "-----BEGIN PUBLIC KEY-----\\nMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmWuuQW+dcqWtrC7mocrc\\nj0pH9IsKgmcA84DaKoJ1obgX2zWEw3HqN6f3Oofs4nF60fLudlxoSoAPNzeJXV48\\nKlUz+PpMRt/lq9tXbe+/xQRXIL7SFqmeeXEba+J/8/pUFWEi91AP+OULoHG5QF+J\\nPiVpuBI0WXrzkIKgFpOcedOxaLh8Px0Xjk9IWQoVb9rCWQMnEdcEA6mUp9ASuZLV\\n5zjDSL6xu7pP5KQoFhLxS2Fx7Dw77v479qGIM6Wl+1/ceMPTVona2foEb9MvtM0n\\nvzZNXX4h1NDvsxzXtXMCw+sZlUa4w5GVQHcDFe6HixhBFycwqn2gf2ev5M0MaJ0X\\ntsaHpQOHElZFLT+IXHPqkWCz3aSJYZsTPN8CL0paBaoOQ7jjOS+4iKYZALi0ORAU\\nJER54jaSeYmN+6kvRJej9jYgDE5dtHZ4U+8YfXGgpebL5E3yiExz8l2+aB5OZ+qy\\nYSsjnQUxBFnIw5ybwB5Ug+1n7bDHjW0TVDrVyBa9GSmehwvHkvbzTvNo6Bh8XNgN\\npxv1PAZKPfXxOZkAItSjzUll68c1hH1nli+EmZVhPY4aGDM3kljw4YPfRdJTM7FD\\nsvhM273FWPSbzY9MDNs/D+TTs2X2Wr1icKbAC6/KTOCZq4NFtfj3s4D2u0FoIaMc\\ncUVnq8hYzL/lTknbqu62dhECAwEAAQ==\\n-----END PUBLIC KEY-----",
        true));

    // fetch all voters
    System.out.println("Voters found with findAll():");
    System.out.println("-------------------------------");
    for (Voter voter : repository.findAll()) {
      System.out.println(voter);
    }
    System.out.println();

    // fetch an individual voter
    System.out.println("Voter found with findByFirstName('Tadas'):");
    System.out.println("--------------------------------");
    System.out.println(repository.findByFirstName("Tadas"));

    // fetch voter by personalNo hopefully
    System.out.println("Voter found with findByPersonalNo('49101134312'):");
    System.out.println("--------------------------------");
    System.out.println(repository.findByPersonalNo("49101134312"));

    /*
    System.out.println("Voters found with findByLastName('Smith'):");
    System.out.println("--------------------------------");
    for (Voter voter : repository.findByLastName("Smith")) {
      System.out.println(voter);
    }
    */

  }

}

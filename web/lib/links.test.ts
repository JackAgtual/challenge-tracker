import { applicationUrl } from "./links";

describe("applicationUrl", () => {
  const originalBaseUrl = process.env.APP_BASE_URL;

  function setBaseUrl(value: string | undefined) {
    if (value === undefined) {
      delete process.env.APP_BASE_URL;
    } else {
      process.env.APP_BASE_URL = value;
    }
  }
  beforeEach(() => {
    setBaseUrl("http://10.0.4.16:8080/challenge-tracker");
  });

  afterEach(() => {
    setBaseUrl(originalBaseUrl);
  });

  describe("configuration", () => {
    it("throws when APP_BASE_URL is not set", () => {
      setBaseUrl(undefined);
      expect(() => applicationUrl("/dashboard")).toThrow(/must be configured/);
    });

    it("throws when APP_BASE_URL is an empty string", () => {
      setBaseUrl("");
      expect(() => applicationUrl("/dashboard")).toThrow(/must be configured/);
    });
  });

  describe("base URL with a path prefix", () => {
    it.each([
      ["no trailing slash", "http://10.0.4.16:8080/challenge-tracker"],
      ["one trailing slash", "http://10.0.4.16:8080/challenge-tracker/"],
      [
        "multiple trailing slashes",
        "http://10.0.4.16:8080/challenge-tracker///",
      ],
    ])("keeps the prefix when the base has %s", (_label, base) => {
      setBaseUrl(base);
      expect(applicationUrl("/dashboard").href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/dashboard"
      );
    });

    it("keeps the prefix for a nested prefix", () => {
      setBaseUrl("http://example.com/apps/challenge-tracker");
      expect(applicationUrl("/dashboard").href).toBe(
        "http://example.com/apps/challenge-tracker/dashboard"
      );
    });
  });

  describe("base URL with no path (origin only)", () => {
    it.each([
      ["without trailing slash", "http://10.0.4.16:8080"],
      ["with trailing slash", "http://10.0.4.16:8080/"],
    ])("works %s", (_label, base) => {
      setBaseUrl(base);
      expect(applicationUrl("/dashboard").href).toBe(
        "http://10.0.4.16:8080/dashboard"
      );
    });

    it("preserves the port", () => {
      setBaseUrl("http://10.0.4.16:8080");
      expect(applicationUrl("/x").port).toBe("8080");
    });

    it("works with https and no explicit port", () => {
      setBaseUrl("https://example.com");
      expect(applicationUrl("/dashboard").href).toBe(
        "https://example.com/dashboard"
      );
    });
  });

  describe("path handling", () => {
    it.each([
      ["a leading slash", "/dashboard"],
      ["no leading slash", "dashboard"],
      ["multiple leading slashes", "///dashboard"],
    ])("treats a path with %s the same way", (_label, path) => {
      expect(applicationUrl(path).href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/dashboard"
      );
    });

    it("handles nested paths", () => {
      expect(applicationUrl("/challenges/create").href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/challenges/create"
      );
    });

    it("preserves a trailing slash on the path", () => {
      expect(applicationUrl("/dashboard/").href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/dashboard/"
      );
    });

    it("preserves query strings and hashes", () => {
      expect(applicationUrl("/dashboard?tab=active&page=2#top").href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/dashboard?tab=active&page=2#top"
      );
    });
  });

  describe("empty and default path", () => {
    it("defaults to the base URL with a single trailing slash", () => {
      expect(applicationUrl().href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/"
      );
    });

    it("treats an empty string like the default", () => {
      expect(applicationUrl("").href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/"
      );
    });

    it("treats a lone slash like the default", () => {
      expect(applicationUrl("/").href).toBe(
        "http://10.0.4.16:8080/challenge-tracker/"
      );
    });

    it("returns the origin root when the base has no path", () => {
      setBaseUrl("http://10.0.4.16:8080");
      expect(applicationUrl().href).toBe("http://10.0.4.16:8080/");
    });
  });
});

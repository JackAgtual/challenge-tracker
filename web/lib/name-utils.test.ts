import { components } from "@/types/api";
import {
  getChallengeNameAndDuration,
  getDisplayNameFromUser,
  getInitials,
} from "./name-utils";

type User = components["schemas"]["UserResponse"];
type Challenge = components["schemas"]["ChallengeResponse"];

describe("name-utils", () => {
  const firstName = "John";
  const lastName = "Doe";
  const username = "jdoe12";

  let validUser: User;

  beforeEach(() => {
    validUser = {
      id: 1,
      firstName,
      lastName,
      username,
    };
  });

  describe("getDisplayNameFromUser", () => {
    const expectedDisplayName = "jdoe12 (John Doe)";

    it("Returns valid display name", () => {
      expect(getDisplayNameFromUser(validUser)).toEqual(expectedDisplayName);
    });

    it("handles all undefined parameters", () => {
      const user: User = {
        id: 1,
      };

      expect(getDisplayNameFromUser(user)).toEqual("UNK (UNK UNK)");
    });

    it("handles partially defined user", () => {
      const user: User = {
        id: 1,
        firstName,
        username,
      };

      expect(getDisplayNameFromUser(user)).toEqual("jdoe12 (John UNK)");
    });
  });

  describe("getChallengeNameAndDuration", () => {
    it("get correct text", () => {
      const challenge: Challenge = {
        id: 1,
        durationDays: 30,
        name: "75 hard",
        owner: { id: 2 },
        status: "PENDING",
      };

      expect(getChallengeNameAndDuration(challenge)).toEqual(
        "75 hard - 30 days"
      );
    });

    it("accounts for singular/plural days", () => {
      const challenge: Challenge = {
        id: 1,
        durationDays: 1,
        name: "one day wonder",
        owner: { id: 2 },
        status: "PENDING",
      };

      expect(getChallengeNameAndDuration(challenge)).toEqual(
        "one day wonder - 1 day"
      );
    });
  });

  describe("getInitials", () => {
    it("returns correct initials", () => {
      const initials = getInitials(validUser);
      expect(initials).toEqual("JD");
    });

    it("handles undefined first name", () => {
      const user = validUser;
      user.firstName = undefined;

      const initials = getInitials(user);

      expect(initials).toEqual("D");
    });

    it("handles undefined last name", () => {
      const user = validUser;
      user.lastName = undefined;

      const initials = getInitials(user);

      expect(initials).toEqual("J");
    });

    it("handles undefined first and last name", () => {
      const user = validUser;
      user.firstName = undefined;
      user.lastName = undefined;

      const initials = getInitials(user);

      expect(initials).toEqual("?");
    });

    it("converts to uppercase", () => {
      const user = validUser;
      user.firstName = "john";
      user.lastName = "doe";

      const initials = getInitials(user);

      expect(initials).toEqual("JD");
    });
  });
});

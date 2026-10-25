import { components } from "@/types/api";
import {
  getChallengeNameAndDuration,
  getDisplayNameFromUser,
} from "./name-utils";

type User = components["schemas"]["UserResponse"];
type Challenge = components["schemas"]["ChallengeResponse"];

describe("getDisplayNameFromUser", () => {
  const firstName = "John";
  const lastName = "Doe";
  const username = "jdoe12";

  const expectedDisplayName = "jdoe12 (John Doe)";

  it("Returns valid display name", () => {
    const user: User = {
      id: 1,
      firstName,
      lastName,
      username,
    };

    expect(getDisplayNameFromUser(user)).toEqual(expectedDisplayName);
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

    expect(getChallengeNameAndDuration(challenge)).toEqual("75 hard - 30 days");
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

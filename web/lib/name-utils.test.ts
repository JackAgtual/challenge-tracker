import { components } from "@/types/api";
import {
  getDisplayNameFromParticipant,
  getDisplayNameFromUser,
} from "./name-utils";

type User = components["schemas"]["UserResponse"];

const firstName = "John";
const lastName = "Doe";
const username = "jdoe12";

const expectedDisplayName = "jdoe12 (John Doe)";

describe("getDisplayNameFromParticipant", () => {
  it("Returns valid display name", () => {
    const participant: components["schemas"]["ParticipantResponse"] = {
      id: 1,
      firstName,
      lastName,
      username,
      ready: false,
      isChallengeOwner: false,
      joinedAt: "2026-10-10",
    };

    expect(getDisplayNameFromParticipant(participant)).toEqual(
      expectedDisplayName
    );
  });
});

describe("getDisplayNameFromUser", () => {
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

import { components } from "@/types/api";
import { getDisplayNameFromUser } from "./name-utils";

type User = components["schemas"]["UserResponse"];

const firstName = "John";
const lastName = "Doe";
const username = "jdoe12";

const expectedDisplayName = "jdoe12 (John Doe)";

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

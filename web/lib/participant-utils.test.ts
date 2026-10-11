import { components } from "@/types/api";
import { getParticipantDisplayName } from "./participant-utils";

describe("getParticipantDisplayName", () => {
  it("Returns valid display name", () => {
    const participant: components["schemas"]["ParticipantResponse"] = {
      id: 1,
      firstName: "John",
      lastName: "Doe",
      username: "jdoe12",
      ready: false,
      isChallengeOwner: false,
      joinedAt: "2026-10-10",
    };

    expect(getParticipantDisplayName(participant)).toEqual("jdoe12 (John Doe)");
  });
});

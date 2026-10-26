import { CenterContainer } from "@/components/CenterContainer";
import { ChallengeForm } from "@/components/ChallengeForm";
import { client } from "@/lib/api-client";
import { TChallengeDataFormSchema } from "@/types/form-types";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: string }>;
}) {
  const { challengeId: challengeIdStr } = await params;
  const challengeId = Number(challengeIdStr);

  const res = await client.GET("/challenges/{challengeId}", {
    params: { path: { challengeId } },
  });

  const defaultVals: TChallengeDataFormSchema = res.error
    ? { name: "", durationDays: 0 }
    : {
        name: res.data.challenge.name,
        durationDays: res.data.challenge.durationDays || 0,
      };

  return (
    <CenterContainer>
      <ChallengeForm
        action="EDIT"
        challengeId={challengeId}
        defaultValues={defaultVals}
      />
    </CenterContainer>
  );
}
